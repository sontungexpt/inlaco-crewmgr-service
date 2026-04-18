package com.inlaco.crewmgrservice.feature.crew.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.model.CrewProfileSearchCriteria;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewIdentityUseCase;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.application.port.out.CrewProfileRepository;
import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewStatus;
import com.inlaco.crewmgrservice.feature.crew.domain.model.ApplyLaborContractCommand;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
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
  public List<CrewProfile> getProfilesByCardIds(Iterable<String> cardIds) {
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
}
