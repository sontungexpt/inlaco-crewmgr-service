package com.inlaco.crewmgrservice.feature.crewmobilization.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.ContractQueryUseCase;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationCommandUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out.CrewMobilizationRepository;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.event.NewCrewMobilizationEvent;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.AssignedCrew;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class CrewMobilizationCommandService implements CrewMobilizationCommandUseCase {

  private final CrewMobilizationRepository crewMobilizationRepository;
  private final CrewUseCase crewUseCase;
  private final ApplicationEventPublisher eventPublisher;
  private final ContractQueryUseCase contractQueryUseCase;
  private final UploadDispatcher uploadDispatcher;

  @Override
  @Transactional
  public CrewMobilization createMobilization(
      CrewMobilization mobilization, String shipImageAssetId, User user) {
    log.debug("Starting schedule creation with details: {}", mobilization);

    var contract = contractQueryUseCase.getContract(mobilization.getContractId(), null, user);

    if (!contract.isSigned() || contract.isCancelled() || contract.isExpired()) {
      log.warn("Schedule creation failed: Contract not signed");
      throw new IllegalArgumentException("Contract not signed");
    }

    if (!(contract instanceof CrewSupplyContract supplyContract)) {
      log.warn("Schedule creation failed: Contract not found");
      throw new ResourceNotFoundException(
          CrewSupplyContract.class, "id", mobilization.getContractId());
    }

    mobilization.setPartnerAccountId(supplyContract.getPartners().get(0).getAccountId());
    mobilization.setCrewRentalRequestId(supplyContract.getCrewRentalRequestId());

    mobilization.getShipInfo().setImoNumber(supplyContract.getShipInfo().getImoNumber());
    if (shipImageAssetId != null) {
      log.debug("Using upload image for ship image");
      mobilization
          .getShipInfo()
          .setImage(uploadDispatcher.fetch(AssetType.SHIP_IMAGE, shipImageAssetId));
    } else {
      log.debug("Using contract image for ship image");
      mobilization.getShipInfo().setImage(supplyContract.getShipInfo().getImage());
    }

    List<String> employeeCardIds =
        mobilization.getCrews().stream().map(AssignedCrew::getEmployeeCardId).toList();
    List<CrewProfile> profiles = crewUseCase.getProfilesByEmployeeCardIds(employeeCardIds);

    if (profiles.size() != employeeCardIds.size()) {
      log.warn("Schedule creation failed: Some crew members do not exist");
      throw new IllegalArgumentException("Some crew members do not exist");
    }

    Map<String, CrewProfile> profileMap =
        profiles.stream()
            .collect(Collectors.toMap(CrewProfile::getEmployeeCardId, Function.identity()));

    mobilization
        .getCrews()
        .forEach(
            crew -> {
              CrewProfile profile = profileMap.get(crew.getEmployeeCardId());
              if (profile != null) {
                crew.setProfileId(profile.getId());
                crew.setAccountId(profile.getAccountId());
              }
            });

    log.debug("Saving schedule to repository");
    var newMobilization = crewMobilizationRepository.save(mobilization);

    log.info(
        "Schedule created successfully [id={}, startDate={}, endDate={}]",
        newMobilization.getId(),
        newMobilization.getStartDate(),
        newMobilization.getEndDate());

    log.debug(
        "Publishing NewCrewMobilizationScheduleEvent for schedule ID: {}", newMobilization.getId());
    eventPublisher.publishEvent(new NewCrewMobilizationEvent(newMobilization));

    return newMobilization;
  }
}
