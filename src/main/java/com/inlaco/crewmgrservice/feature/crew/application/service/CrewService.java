package com.inlaco.crewmgrservice.feature.crew.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.model.CrewProfileSearchCriteria;
import com.inlaco.crewmgrservice.feature.crew.application.model.UpdateCrewProfileAdminCommand;
import com.inlaco.crewmgrservice.feature.crew.application.model.UpdateCrewProfileCrewCommand;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewIdentityUseCase;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.application.port.out.CrewProfileRepository;
import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewStatus;
import com.inlaco.crewmgrservice.feature.crew.domain.model.ApplyLaborContractCommand;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
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
  private final UploadDispatcher uploadDispatcher;

  @Override
  public CrewProfile getProfile(String profileId) {
    log.debug("Fetching crew profile with ID: {}", profileId);
    return crewProfileRepository
        .findById(profileId)
        .orElseThrow(
            () -> {
              log.warn("Crew profile not found with ID: {}", profileId);
              return new ResourceNotFoundException(CrewProfile.class, "id", profileId);
            });
  }

  @Override
  public CrewProfile getProfileForAccount(String accountId) {
    log.debug("Fetching crew profile for account ID: {}", accountId);
    return crewProfileRepository
        .findByAccountId(accountId)
        .orElseThrow(
            () -> {
              log.warn("Crew profile not found for account ID: {}", accountId);
              return new ResourceNotFoundException(CrewProfile.class, "accountId", accountId);
            });
  }

  @Override
  public Page<CrewProfile> getProfiles(CrewProfileSearchCriteria criteria, Pageable pageable) {
    log.debug("Fetching crew profiles with criteria: {}", criteria);
    return crewProfileRepository.findAll(criteria, pageable);
  }

  @Override
  public List<CrewProfile> getProfilesByEmployeeCardIds(Iterable<String> cardIds) {
    log.debug("Fetching crew profiles by employee card IDs");
    return crewProfileRepository.findAllByEmployeeCardId(cardIds);
  }

  @Override
  public void applyLaborContract(ApplyLaborContractCommand command) {
    String accountId = command.accountId();
    log.debug("Applying labor contract for account ID: {}", accountId);
    CrewProfile crewPrrofile =
        crewProfileRepository
            .findByAccountId(accountId)
            .orElseGet(
                () -> {
                  log.info("Creating new crew profile for account ID: {}", accountId);
                  return new CrewProfile();
                });

    crewPrrofile.setFullName(command.fullName());
    crewPrrofile.setAddress(command.address());
    crewPrrofile.setPhoneNumber(command.phone());
    crewPrrofile.setAccountId(accountId);
    crewPrrofile.setBirthDate(command.birthDate());
    crewPrrofile.setProfessionalPosition(command.position());
    crewPrrofile.setEmail(command.email());

    if (crewPrrofile.getEmployeeCardId() == null) {
      crewPrrofile.setEmployeeCardId(crewIdentityUseCase.generateEmployeeCardId());
    }

    if (crewPrrofile.getStatus() == null) {
      crewPrrofile.setStatus(CrewStatus.DRAFT);
    }

    log.info("Saving crew profile for account ID: {}", accountId);
    crewProfileRepository.save(crewPrrofile);
  }

  @Override
  public boolean existsAllByEmployeeCardIds(Iterable<String> employeeIds) {
    log.debug("Checking existence of all crew profiles by employee card IDs");
    return crewProfileRepository.existsAllByEmployeeCardIds(employeeIds);
  }

  @Override
  public CrewProfile adminUpdateProfile(String id, UpdateCrewProfileAdminCommand cmd, User user) {
    CrewProfile profile = getProfile(id);
    log.debug("Admin updating full crew profile. profileId={}", profile.getId());

    cmd.getFullName()
        .ifUpdated(
            v -> {
              log.debug("fullName changed: {} -> {}", profile.getFullName(), v);
              profile.setFullName(v);
            });

    cmd.getEmail()
        .ifUpdated(
            v -> {
              log.debug("email changed: {} -> {}", profile.getEmail(), v);
              profile.setEmail(v);
            });

    cmd.getPhoneNumber()
        .ifUpdated(
            v -> {
              log.debug("phoneNumber changed: {} -> {}", profile.getPhoneNumber(), v);
              profile.setPhoneNumber(v);
            });

    cmd.getAddress()
        .ifUpdated(
            v -> {
              log.debug("address changed");
              profile.setAddress(v);
            });

    cmd.getGender()
        .ifUpdated(
            v -> {
              log.debug("gender changed: {} -> {}", profile.getGender(), v);
              profile.setGender(v);
            });

    cmd.getProfessionalPosition()
        .ifUpdated(
            v -> {
              log.debug("professionalPosition updated");
              profile.setProfessionalPosition(v);
            });

    cmd.getBirthDate()
        .ifUpdated(
            v -> {
              log.debug("birthDate updated");
              profile.setBirthDate(v);
            });

    cmd.getCitizenIdentityCardId()
        .ifUpdated(
            v -> {
              log.debug("socialInsuranceCode updated");
              profile.setCitizenIdentityCardId(v);
            });

    cmd.getCitizenIdentityCardImageFront()
        .ifUpdated(
            v -> {
              log.debug("citizenIdentityCardImageFront updated");
              Asset img = uploadDispatcher.fetch(AssetType.CITIZEN_IDENTITY_CARD, v);
              profile.setCitizenIdentityCardImageFront(img);
            });

    cmd.getCitizenIdentityCardImageBack()
        .ifUpdated(
            v -> {
              log.debug("citizenIdentityCardImageBack updated");
              Asset img = uploadDispatcher.fetch(AssetType.CITIZEN_IDENTITY_CARD, v);
              profile.setCitizenIdentityCardImageBack(img);
            });

    cmd.getSocialInsuranceCode()
        .ifUpdated(
            v -> {
              log.debug("socialInsuranceCode updated");
              profile.setSocialInsuranceCode(v);
            });

    cmd.getSocialInsuranceImageFront()
        .ifUpdated(
            v -> {
              log.debug("socialInsuranceImageFront updated");
              Asset img = uploadDispatcher.fetch(AssetType.SOCIAL_INSURANCE, v);
              profile.setSocialInsuranceImageFront(img);
            });

    cmd.getSocialInsuranceImageBack()
        .ifUpdated(
            v -> {
              log.debug("socialInsuranceImageBack updated");
              Asset img = uploadDispatcher.fetch(AssetType.SOCIAL_INSURANCE, v);
              profile.setSocialInsuranceImageBack(img);
            });

    cmd.getAccidentInsuranceCode()
        .ifUpdated(
            v -> {
              log.debug("accidentInsuranceCode updated");
              profile.setAccidentInsuranceCode(v);
            });

    cmd.getAccidentInsuranceImageFront()
        .ifUpdated(
            v -> {
              log.debug("accidentInsuranceImageFront updated");
              Asset img = uploadDispatcher.fetch(AssetType.ACCIDENT_INSURANCE, v);
              profile.setAccidentInsuranceImageFront(img);
            });

    cmd.getAccidentInsuranceImageBack()
        .ifUpdated(
            v -> {
              log.debug("accidentInsuranceImageBack updated");
              Asset img = uploadDispatcher.fetch(AssetType.ACCIDENT_INSURANCE, v);
              profile.setAccidentInsuranceImageBack(img);
            });

    CrewProfile saved = crewProfileRepository.save(profile);

    log.info("Admin profile update completed. profileId={}", saved.getId());

    return saved;
  }

  @Override
  public CrewProfile crewUpdateProfile(String id, UpdateCrewProfileCrewCommand cmd, User user) {
    CrewProfile profile = getProfile(id);
    log.debug("Crew updating full crew profile. profileId={}", profile.getId());

    cmd.getEmail()
        .ifUpdated(
            v -> {
              log.debug("email updated: {} -> {}", profile.getEmail(), v);
              profile.setEmail(v);
            });

    cmd.getPhoneNumber()
        .ifUpdated(
            v -> {
              log.debug("phoneNumber updated");
              profile.setPhoneNumber(v);
            });

    cmd.getAddress()
        .ifUpdated(
            v -> {
              log.debug("address updated");
              profile.setAddress(v);
            });

    cmd.getCitizenIdentityCardId()
        .ifUpdated(
            v -> {
              log.debug("socialInsuranceCode updated");
              profile.setCitizenIdentityCardId(v);
            });

    cmd.getCitizenIdentityCardImageFront()
        .ifUpdated(
            v -> {
              log.debug("citizenIdentityCardImageFront updated");
              Asset img = uploadDispatcher.fetch(AssetType.CITIZEN_IDENTITY_CARD, v);
              profile.setCitizenIdentityCardImageFront(img);
            });

    cmd.getCitizenIdentityCardImageBack()
        .ifUpdated(
            v -> {
              log.debug("citizenIdentityCardImageBack updated");
              Asset img = uploadDispatcher.fetch(AssetType.CITIZEN_IDENTITY_CARD, v);
              profile.setCitizenIdentityCardImageBack(img);
            });

    cmd.getSocialInsuranceCode()
        .ifUpdated(
            v -> {
              log.debug("socialInsuranceCode updated");
              profile.setSocialInsuranceCode(v);
            });

    cmd.getSocialInsuranceImageFront()
        .ifUpdated(
            v -> {
              log.debug("socialInsuranceImageFront updated");
              Asset img = uploadDispatcher.fetch(AssetType.SOCIAL_INSURANCE, v);
              profile.setSocialInsuranceImageFront(img);
            });

    cmd.getSocialInsuranceImageBack()
        .ifUpdated(
            v -> {
              log.debug("socialInsuranceImageBack updated");
              Asset img = uploadDispatcher.fetch(AssetType.SOCIAL_INSURANCE, v);
              profile.setSocialInsuranceImageBack(img);
            });

    cmd.getAccidentInsuranceCode()
        .ifUpdated(
            v -> {
              log.debug("accidentInsuranceCode updated");
              profile.setAccidentInsuranceCode(v);
            });

    cmd.getAccidentInsuranceImageFront()
        .ifUpdated(
            v -> {
              log.debug("accidentInsuranceImageFront updated");
              Asset img = uploadDispatcher.fetch(AssetType.ACCIDENT_INSURANCE, v);
              profile.setAccidentInsuranceImageFront(img);
            });

    cmd.getAccidentInsuranceImageBack()
        .ifUpdated(
            v -> {
              log.debug("accidentInsuranceImageBack updated");
              Asset img = uploadDispatcher.fetch(AssetType.ACCIDENT_INSURANCE, v);
              profile.setAccidentInsuranceImageBack(img);
            });

    CrewProfile saved = crewProfileRepository.save(profile);

    log.info("Crew profile self-update completed. profileId={}", saved.getId());

    return saved;
  }
}
