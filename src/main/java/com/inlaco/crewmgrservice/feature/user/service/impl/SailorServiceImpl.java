package com.inlaco.crewmgrservice.feature.user.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.inlaco.crewmgrservice.exceptions.ResourceNotFoundException;
import com.inlaco.crewmgrservice.feature.contract.model.LaborContract;
import com.inlaco.crewmgrservice.feature.contract.model.LaborParty;
import com.inlaco.crewmgrservice.feature.user.dto.BasicProfileDTO;
import com.inlaco.crewmgrservice.feature.user.dto.SailorFilterable;
import com.inlaco.crewmgrservice.feature.user.enums.WorkStatus;
import com.inlaco.crewmgrservice.feature.user.event.SailorOfficalEvent;
import com.inlaco.crewmgrservice.feature.user.model.CandidateProfile;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import com.inlaco.crewmgrservice.feature.user.model.User;
import com.inlaco.crewmgrservice.feature.user.repository.CustomSailorRepository;
import com.inlaco.crewmgrservice.feature.user.repository.SailorProfileRepository;
import com.inlaco.crewmgrservice.feature.user.service.CandidateService;
import com.inlaco.crewmgrservice.feature.user.service.SailorService;
import com.inlaco.crewmgrservice.feature.user.service.UserService;
import com.inlaco.crewmgrservice.feature.user.service.state.candidate.ReviewServiceFactory;
import com.inlaco.crewmgrservice.utils.JsonMergePatchUtils;
import java.time.Instant;
import java.time.Year;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class SailorServiceImpl implements SailorService {

  private final UserService userService;
  private final SailorProfileRepository sailorProfileRepository;
  private final JsonMergePatchUtils jsonMergePatchUtils;
  private final CandidateService candidateService;
  private final CustomSailorRepository customSailorRepository;
  private final ApplicationEventPublisher applicationEventPublisher;
  private final ReviewServiceFactory reviewServiceFactory;

  @Override
  public String generateSailorCardId() {
    String currentYear = String.format("%04d", Year.now().getValue());
    String currentIndex = String.format("%05d", customSailorRepository.countSailorHasCardId() + 1);
    var cardId = currentYear + currentIndex;
    log.debug("Generated sailor card id: {}", cardId);
    return cardId;
  }

  private BasicProfileDTO toBasicProfileDTO(SailorProfile sailorProfile) {
    return BasicProfileDTO.builder()
        .id(sailorProfile.getId())
        .fullName(sailorProfile.getFullName())
        .email(sailorProfile.getEmail())
        .address(sailorProfile.getAddress())
        // .file(sailorProfile.get())
        .phoneNumber(sailorProfile.getPhoneNumber())
        .gender(sailorProfile.getGender())
        .birthDate(sailorProfile.getBirthDate())
        .build();
  }

  @Override
  public Page<BasicProfileDTO> getAllSailors(SailorFilterable filterable, Pageable pageable) {
    return customSailorRepository
        .fetchAllSailors(filterable, pageable)
        .map(this::toBasicProfileDTO);
  }

  @Override
  public SailorProfile findSailorProfileById(String sailorId) {
    log.info("Find sailor profile with id: {}", sailorId);
    return sailorProfileRepository
        .findById(sailorId)
        .orElseThrow(() -> new ResourceNotFoundException(SailorProfile.class, "id", sailorId));
  }

  @Override
  public SailorProfile findSailorProfileByIdOrNull(String sailorId) {
    log.info("Find sailor profile with id: {}", sailorId);
    return sailorProfileRepository.findById(sailorId).orElse(null);
  }

  @Override
  public SailorProfile findSailorProfileByAccountIdOrNull(String accountId) {
    log.info("Find sailor profile with accountId: {}", accountId);
    return sailorProfileRepository.findByAccountId(new ObjectId(accountId)).orElse(null);
  }

  @Override
  public Page<SailorProfile> searchSailors(String query, Criteria filter, Pageable pageable) {
    return customSailorRepository.searchSailors(query, filter, pageable);
  }

  @Override
  public SailorProfile updateSailorProfile(String sailorId, JsonNode patch) {
    return jsonMergePatchUtils.patch(sailorId, SailorProfile.class, patch);
  }

  @Override
  public SailorProfile findMySailorProfile(User user) {
    return sailorProfileRepository
        .findByAccountId(new ObjectId(user.getId()))
        .orElseThrow(
            () -> new ResourceNotFoundException(SailorProfile.class, "accountId", user.getId()));
  }

  @Override
  public SailorProfile saveSailorProfile(SailorProfile sailorProfile) {
    return sailorProfileRepository.save(sailorProfile);
  }

  @Override
  public SailorProfile findSailorProfileByAccountId(String accountId) {
    return sailorProfileRepository
        .findByAccountId(new ObjectId(accountId))
        .orElseThrow(
            () -> new ResourceNotFoundException(SailorProfile.class, "accountId", accountId));
  }

  @Override
  public boolean existsSailorProfileById(String sailorId) {
    return sailorProfileRepository.existsById(sailorId);
  }

  @Override
  @Transactional
  public void makeSailorOfficial(LaborContract contract) {
    ObjectId sailorAccountId = contract.getEmployeeId();
    SailorProfile sailorProfile =
        sailorProfileRepository
            .findByAccountId(contract.getEmployeeId())
            .orElseGet(() -> new SailorProfile());

    LaborParty sailorParty = (LaborParty) contract.getPartners().get(0);
    sailorProfile.setFullName(sailorParty.getRepresenter());
    sailorProfile.setAddress(sailorParty.getAddress());
    sailorProfile.setPhoneNumber(sailorParty.getPhone());
    sailorProfile.setAccountId(contract.getEmployeeId());
    sailorProfile.setBirthDate(sailorParty.getBirthDate());
    sailorProfile.setProfessionalPosition(contract.getPosition());

    if (sailorProfile.getCardId() == null) {
      sailorProfile.setCardId(generateSailorCardId());
    }

    if (sailorProfile.getWorkStatus() == null) {
      sailorProfile.setWorkStatus(WorkStatus.AVAILABLE);
    }

    if (sailorProfile.getJoinedCompanyAt() == null) {
      sailorProfile.setJoinedCompanyAt(Instant.now());
    }
    saveSailorProfile(sailorProfile);

    candidateService.reviewCandidate(
        contract.getCandidateProfileId().toHexString(), CandidateProfile.Status.HIRED, true);

    userService.updateToSailor(sailorAccountId.toHexString());
    applicationEventPublisher.publishEvent(new SailorOfficalEvent(this, contract));
  }

  @Override
  public List<SailorProfile> findSailorProfilesByCardIds(Iterable<String> sailorIds) {
    return sailorProfileRepository.findByCardIdIn(sailorIds);
  }

  @Override
  public SailorProfile findSailorProfileByCardId(String cardId) {
    return sailorProfileRepository
        .findByCardId(cardId)
        .orElseThrow(() -> new ResourceNotFoundException(SailorProfile.class, "cardId", cardId));
  }

  @Override
  public SailorProfile findSailorProfileByAccountIdOrNull(ObjectId sailorId) {
    return sailorProfileRepository.findByAccountId(sailorId).orElse(null);
  }
}
