package com.inlaco.crewmgrservice.feature.crew.application.service;

import com.inlaco.crewmgrservice.application.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.domain.model.LaborParty;
import com.inlaco.crewmgrservice.feature.crew.application.model.CrewProfileSearchCriteria;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewIdentityUseCase;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.application.port.out.CrewProfileRepository;
import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewStatus;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.recruitment.application.port.in.RecruitmentReviewUseCase;
import com.inlaco.crewmgrservice.feature.recruitment.domain.enums.ApplicationStatus;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CrewService implements CrewUseCase {

  private final CrewProfileRepository crewProfileRepository;
  private final CrewIdentityUseCase crewIdentityUseCase;
  private final RecruitmentReviewUseCase recruitmentReviewUseCase;

  @Override
  public CrewProfile getProfile(String profileId) {
    return crewProfileRepository
        .findById(profileId)
        .orElseThrow(() -> new ResourceNotFoundException(CrewProfile.class, "id", profileId));
  }

  @Override
  public CrewProfile getProfileForAccount(String accountId) {
    return crewProfileRepository
        .findByAccountId(accountId)
        .orElseThrow(
            () -> new ResourceNotFoundException(CrewProfile.class, "accountId", accountId));
  }

  @Override
  public Page<CrewProfile> getProfiles(CrewProfileSearchCriteria criteria, Pageable pageable) {
    return crewProfileRepository.findAll(criteria, pageable);
  }

  @Override
  public List<CrewProfile> getProfilesByCardIds(Iterable<String> cardIds) {
    return crewProfileRepository.findByEmployeeCardIdIn(cardIds);
  }

  @Override
  public void makeCrewOfficial(LaborContract contract) {
    String accountId = contract.getEmployeeId().toHexString();
    CrewProfile crewPrrofile =
        crewProfileRepository.findByAccountId(accountId).orElseGet(() -> new CrewProfile());

    LaborParty sailorParty = (LaborParty) contract.getPartners().get(0);
    crewPrrofile.setFullName(sailorParty.getRepresenter());
    crewPrrofile.setAddress(sailorParty.getAddress());
    crewPrrofile.setPhoneNumber(sailorParty.getPhone());
    crewPrrofile.setAccountId(accountId);
    crewPrrofile.setBirthDate(sailorParty.getBirthDate());
    crewPrrofile.setProfessionalPosition(contract.getPosition());
    crewPrrofile.setEmail(sailorParty.getEmail());

    if (crewPrrofile.getEmployeeCardId() == null) {
      crewPrrofile.setEmployeeCardId(crewIdentityUseCase.generateEmployeeCardId());
    }

    if (crewPrrofile.getStatus() == null) {
      crewPrrofile.setStatus(CrewStatus.READY_FOR_ASSIGNMENT);
    }

    crewProfileRepository.save(crewPrrofile);

    recruitmentReviewUseCase.reviewApplication(
        contract.getCandidateProfileId().toHexString(), ApplicationStatus.HIRED);

    // crewPrrofile.updateToSailor(accountId.toHexString());
    // applicationEventPublisher.publishEvent(new SailorOfficalEvent(this, contract));
  }
}
