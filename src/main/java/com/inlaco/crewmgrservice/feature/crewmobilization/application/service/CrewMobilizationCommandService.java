package com.inlaco.crewmgrservice.feature.crewmobilization.application.service;

import com.inlaco.crewmgrservice.feature.contract.application.port.in.ContractQueryUseCase;
import com.inlaco.crewmgrservice.feature.contract.domain.model.CrewSupplyContract;
import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationCommandUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out.CrewMobilizationAssignmentRepository;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.out.CrewMobilizationRepository;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.error.CrewMobilizationErrorCode;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.event.NewCrewMobilizationEvent;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.exception.CrewAssignmentOverlapException;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.exception.CrewAssignmentOverlapException.ConflictAssignment;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.exception.CrewMobilizationValidationException;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilizationAssignment;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import java.util.ArrayList;
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

  private final CrewMobilizationRepository mobilizationRepository;

  private final CrewMobilizationAssignmentRepository assignmentRepository;

  private final CrewUseCase crewUseCase;

  private final ApplicationEventPublisher eventPublisher;

  private final ContractQueryUseCase contractQueryUseCase;

  private final UploadDispatcher uploadDispatcher;

  private final ShipScheduleUseCase shipScheduleUseCase;

  @Override
  @Transactional
  public CrewMobilization createMobilization(
      CrewMobilization mobilization,
      List<CrewMobilizationAssignment> assignments,
      String shipImageAssetId,
      User user) {

    log.debug("Starting mobilization creation: {}", mobilization);

    CrewSupplyContract contract = getAndValidateContract(mobilization.getContractId(), user);

    validateAssignments(assignments);

    List<String> employeeCardIds =
        assignments.stream().map(CrewMobilizationAssignment::getEmployeeCardId).toList();

    validateAssignmentOverlap(assignments, employeeCardIds);

    List<CrewProfile> crewProfiles = crewUseCase.getProfilesByEmployeeCardIds(employeeCardIds);

    if (crewProfiles.size() != employeeCardIds.size()) {
      throw new CrewMobilizationValidationException(
          CrewMobilizationErrorCode.CREW_MOBILIZATION_CREW_NOT_FOUND,
          "Some crew members do not exist");
    }

    Map<String, CrewProfile> crewProfileMap =
        crewProfiles.stream()
            .collect(Collectors.toMap(CrewProfile::getEmployeeCardId, Function.identity()));

    enrichMobilization(mobilization, contract, shipImageAssetId);

    CrewMobilization savedMobilization = mobilizationRepository.save(mobilization);

    enrichAssignments(assignments, crewProfileMap, contract, savedMobilization);

    assignmentRepository.saveAll(assignments);

    eventPublisher.publishEvent(new NewCrewMobilizationEvent(savedMobilization, crewProfiles));

    log.info("Mobilization created successfully: {}", savedMobilization.getId());

    return savedMobilization;
  }

  private CrewSupplyContract getAndValidateContract(String contractId, User user) {

    var contract = contractQueryUseCase.getContract(contractId, null, user);

    if (!(contract instanceof CrewSupplyContract supplyContract)) {

      throw new CrewMobilizationValidationException(
          CrewMobilizationErrorCode.CREW_MOBILIZATION_CONTRACT_NOT_FOUND,
          "Crew supply contract not found");
    }

    if (!supplyContract.isSigned() || supplyContract.isCancelled() || supplyContract.isExpired()) {

      throw new CrewMobilizationValidationException(
          CrewMobilizationErrorCode.CREW_MOBILIZATION_CONTRACT_NOT_ACTIVE,
          "Contract is not active");
    }

    return supplyContract;
  }

  private void validateAssignments(List<CrewMobilizationAssignment> assignments) {

    if (assignments == null || assignments.isEmpty()) {

      throw new CrewMobilizationValidationException(
          CrewMobilizationErrorCode.CREW_MOBILIZATION_INVALID_ASSIGNMENTS,
          "Assignments cannot be empty");
    }

    for (CrewMobilizationAssignment assignment : assignments) {

      if (assignment.getStartDate() == null || assignment.getEndDate() == null) {

        throw new CrewMobilizationValidationException(
            CrewMobilizationErrorCode.CREW_MOBILIZATION_INVALID_ASSIGNMENT_DATE,
            "Assignment dates cannot be null");
      }

      if (!assignment.getStartDate().isBefore(assignment.getEndDate())) {

        throw new CrewMobilizationValidationException(
            CrewMobilizationErrorCode.CREW_MOBILIZATION_INVALID_ASSIGNMENT_DATE,
            "Assignment start date must be before end date");
      }
    }
  }

  private void validateAssignmentOverlap(
      List<CrewMobilizationAssignment> assignments, List<String> employeeCardIds) {

    Map<String, List<CrewMobilizationAssignment>> assignmentsByEmployeeCardId =
        assignments.stream()
            .collect(Collectors.groupingBy(CrewMobilizationAssignment::getEmployeeCardId));

    List<CrewMobilizationAssignment> existingAssignments =
        assignmentRepository.findNonEndedAssignmentsByEmployeeCardIds(employeeCardIds);

    Map<String, List<CrewMobilizationAssignment>> existingAssignmentMap =
        existingAssignments.stream()
            .collect(Collectors.groupingBy(CrewMobilizationAssignment::getEmployeeCardId));

    List<ConflictAssignment> conflicts = new ArrayList<>();

    for (Map.Entry<String, List<CrewMobilizationAssignment>> entry :
        assignmentsByEmployeeCardId.entrySet()) {

      String employeeCardId = entry.getKey();

      List<CrewMobilizationAssignment> newAssignments = entry.getValue();

      List<CrewMobilizationAssignment> existingCrewAssignments =
          existingAssignmentMap.getOrDefault(employeeCardId, List.of());

      for (CrewMobilizationAssignment newAssignment : newAssignments) {

        boolean overlap = false;
        for (CrewMobilizationAssignment existing : existingCrewAssignments) {

          overlap =
              newAssignment.getStartDate().isBefore(existing.getEndDate())
                  && newAssignment.getEndDate().isAfter(existing.getStartDate());

          if (overlap) {
            conflicts.add(
                new ConflictAssignment(
                    employeeCardId,
                    "Crew already mobbilized in overlapping period",
                    existing.getStartDate(),
                    existing.getEndDate()));
          }
        }

        if (!overlap
            && shipScheduleUseCase.hasAssignmentOverlapByEmployeeCardId(
                newAssignment.getEmployeeCardId(),
                newAssignment.getStartDate(),
                newAssignment.getEndDate())) {

          conflicts.add(
              new ConflictAssignment(
                  employeeCardId,
                  "Crew already in ship schedule in overlapping period",
                  newAssignment.getStartDate(),
                  newAssignment.getEndDate()));
        }
      }

      if (!conflicts.isEmpty()) {
        throw new CrewAssignmentOverlapException(conflicts);
      }
    }
  }

  private void enrichMobilization(
      CrewMobilization mobilization, CrewSupplyContract contract, String shipImageAssetId) {
    log.debug("Enriching mobilization");

    mobilization.setCrewRentalRequestId(contract.getCrewRentalRequestId());
    mobilization.setContractId(contract.getId());
    mobilization.setPartnerAccountId(contract.getPartners().get(0).getAccountId());
    mobilization.getShipInfo().setImoNumber(contract.getShipInfo().getImoNumber());

    if (shipImageAssetId != null) {
      log.debug("Fetching ship image with ID: {}", shipImageAssetId);
      mobilization
          .getShipInfo()
          .setImage(uploadDispatcher.fetch(AssetType.SHIP_IMAGE, shipImageAssetId));

    } else {
      log.debug("Using default ship image");
      mobilization.getShipInfo().setImage(contract.getShipInfo().getImage());
    }
  }

  private void enrichAssignments(
      List<CrewMobilizationAssignment> assignments,
      Map<String, CrewProfile> profileMap,
      CrewSupplyContract contract,
      CrewMobilization mobilization) {

    log.debug("Enriching assignments");

    for (CrewMobilizationAssignment assignment : assignments) {

      CrewProfile profile = profileMap.get(assignment.getEmployeeCardId());

      if (profile == null) {

        throw new CrewMobilizationValidationException(
            CrewMobilizationErrorCode.CREW_MOBILIZATION_CREW_NOT_FOUND, "Crew profile not found");
      }

      if (profile.getAccountId() == null) {

        throw new CrewMobilizationValidationException(
            CrewMobilizationErrorCode.CREW_MOBILIZATION_CREW_HAS_NO_ACCOUNT,
            "Crew member has no account");
      }

      assignment.setProfileId(profile.getId());
      assignment.setAccountId(profile.getAccountId());
      assignment.setShipIMO(contract.getShipInfo().getImoNumber());
      assignment.setPartnerAccountId(contract.getPartners().get(0).getAccountId());
      assignment.setCrewRentalRequestId(contract.getCrewRentalRequestId());
      assignment.setContractId(contract.getId());
      assignment.setMobilizationId(mobilization.getId());
    }
  }
}
