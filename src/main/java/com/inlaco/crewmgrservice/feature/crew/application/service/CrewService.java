package com.inlaco.crewmgrservice.feature.crew.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.model.CrewProfileSearchCriteria;
import com.inlaco.crewmgrservice.feature.crew.application.model.UpdateCrewProfileAdminCommand;
import com.inlaco.crewmgrservice.feature.crew.application.model.UpdateCrewProfileCrewCommand;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewIdentityUseCase;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.application.port.out.CrewProfileRepository;
import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewOperationalStatus;
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

/**
 * Service implementation for managing crew profiles and operations.
 *
 * <p>This service provides comprehensive crew profile management functionality including profile
 * retrieval, creation, updates, and search operations. It integrates with identity management for
 * crew operations and handles file uploads for profile assets.
 *
 * <p>The service maintains crew profiles with proper audit logging and exception handling, ensuring
 * data integrity and providing a reliable interface for crew management operations throughout the
 * system.
 *
 * @author Trần Võ Sơn Tùng
 * @version 1.0
 * @since 1.0
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class CrewService implements CrewUseCase {

  private final CrewProfileRepository crewProfileRepository;
  private final CrewIdentityUseCase crewIdentityUseCase;
  private final UploadDispatcher uploadDispatcher;

  /**
   * Retrieves a crew profile by its unique identifier.
   *
   * <p>This method fetches a specific crew profile from the repository using the provided profile
   * ID. If the profile is not found, it throws a ResourceNotFoundException to indicate the missing
   * resource.
   *
   * @param profileId the unique identifier of the crew profile to retrieve
   * @return the CrewProfile with the specified ID
   * @throws ResourceNotFoundException if no profile exists with the given ID
   */
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

  /**
   * Retrieves a crew profile associated with a specific account.
   *
   * <p>This method finds the crew profile linked to the provided account ID. If no profile is found
   * for the account, it throws a ResourceNotFoundException.
   *
   * @param accountId the account ID to find the crew profile for
   * @return the CrewProfile associated with the account
   * @throws ResourceNotFoundException if no profile exists for the account
   */
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

  /**
   * Searches for crew profiles based on specified criteria.
   *
   * <p>This method performs a paginated search of crew profiles using the provided search criteria.
   * The criteria can include various filters like name, status, and other profile attributes.
   *
   * @param criteria the search criteria to filter crew profiles
   * @param pageable pagination and sorting information
   * @return a Page of CrewProfile matching the criteria
   */
  @Override
  public Page<CrewProfile> getProfiles(CrewProfileSearchCriteria criteria, Pageable pageable) {
    log.debug("Fetching crew profiles with criteria: {}", criteria);
    CrewProfileRepository.CrewProfileSearchCriteria searchCriteria =
        CrewProfileRepository.CrewProfileSearchCriteria.builder()
            .keyword(criteria.keyword())
            .professionalPosition(criteria.professionalPosition())
            .official(criteria.official())
            .workStatus(criteria.workStatus())
            .build();
    return crewProfileRepository.findAll(searchCriteria, pageable);
  }

  /**
   * Retrieves crew profiles by their employee card IDs.
   *
   * <p>This method finds all crew profiles that have the specified employee card IDs, useful for
   * bulk operations and data synchronization.
   *
   * @param cardIds collection of employee card IDs to search for
   * @return list of CrewProfile with matching employee card IDs
   */
  @Override
  public List<CrewProfile> getProfilesByEmployeeCardIds(Iterable<String> cardIds) {
    log.debug("Fetching crew profiles by employee card IDs");
    return crewProfileRepository.findAllByEmployeeCardId(cardIds);
  }

  /**
   * Applies a labor contract to a crew member's profile.
   *
   * <p>This method associates a labor contract with a crew profile, creating a new profile if one
   * doesn't exist for the account. The operation updates the crew profile with contract information
   * and persists the changes.
   *
   * @param command contains the account ID and labor contract details
   */
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
      crewPrrofile.setStatus(CrewOperationalStatus.DRAFT);
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

    cmd.getImage()
        .ifUpdated(
            v -> {
              log.debug("image updated");
              Asset img = uploadDispatcher.fetch(AssetType.CREW_PROFILE, v);
              profile.setImage(img);
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

    cmd.getImage()
        .ifUpdated(
            v -> {
              log.debug("image updated");
              Asset img = uploadDispatcher.fetch(AssetType.CREW_PROFILE, v);
              profile.setImage(img);
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
