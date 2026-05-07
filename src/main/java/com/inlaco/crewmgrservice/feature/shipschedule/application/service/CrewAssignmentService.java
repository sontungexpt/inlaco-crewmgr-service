package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.model.CrewMobilizationSearchCriteria;
import com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in.CrewMobilizationQueryUseCase;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.AssignedCrew;
import com.inlaco.crewmgrservice.feature.crewmobilization.domain.model.CrewMobilization;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.ClientCrewSearchResponse;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.CrewAssignmentResponse;
import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CrewAssignmentService {

  private final ShipScheduleRepository shipScheduleRepository;
  private final CrewUseCase crewUseCase;
  private final CrewMobilizationQueryUseCase crewMobilizationQueryUseCase;

  public List<CrewAssignmentResponse> getCrewByShipImo(String shipImo) {
    List<ShipSchedule> schedules = shipScheduleRepository.findByShipImo(shipImo);

    return schedules.stream()
        .flatMap(
            schedule -> {
              List<String> employeeCardIds = schedule.getEmployeeCardIds();
              if (employeeCardIds == null || employeeCardIds.isEmpty()) {
                return List.<CrewAssignmentResponse>of().stream();
              }

              List<CrewProfile> crewProfiles =
                  crewUseCase.getProfilesByEmployeeCardIds(employeeCardIds);

              return crewProfiles.stream()
                  .map(
                      crew ->
                          CrewAssignmentResponse.builder()
                              .id(crew.getId())
                              .accountId(crew.getAccountId())
                              .fullName(crew.getFullName())
                              .email(crew.getEmail())
                              .phoneNumber(crew.getPhoneNumber())
                              .image(crew.getImage())
                              .address(crew.getAddress())
                              .gender(crew.getGender())
                              .status(crew.getStatus())
                              .professionalPosition(crew.getProfessionalPosition())
                              .birthDate(crew.getBirthDate())
                              .employeeCardId(crew.getEmployeeCardId())
                              .citizenIdentityCardId(crew.getCitizenIdentityCardId())
                              .citizenIdentityCardImageFront(
                                  crew.getCitizenIdentityCardImageFront())
                              .citizenIdentityCardImageBack(crew.getCitizenIdentityCardImageBack())
                              .socialInsuranceCode(crew.getSocialInsuranceCode())
                              .socialInsuranceImageFront(crew.getSocialInsuranceImageFront())
                              .socialInsuranceImageBack(crew.getSocialInsuranceImageBack())
                              .accidentInsuranceCode(crew.getAccidentInsuranceCode())
                              .accidentInsuranceImageFront(crew.getAccidentInsuranceImageFront())
                              .accidentInsuranceImageBack(crew.getAccidentInsuranceImageBack())
                              .shipScheduleId(schedule.getId())
                              .shipImo(schedule.getShipImo())
                              .shipName(schedule.getShipName())
                              .departureTime(schedule.getDepartureTime())
                              .arrivalTime(schedule.getArrivalTime())
                              .departurePort(schedule.getDeparturePort())
                              .arrivalPort(schedule.getArrivalPort())
                              .build());
            })
        .collect(Collectors.toList());
  }

  public List<ClientCrewSearchResponse> getCrewByClientId(String clientId) {
    // Get all mobilizations for this client
    CrewMobilizationSearchCriteria criteria =
        CrewMobilizationSearchCriteria.builder().accountId(clientId).build();

    Pageable pageable = PageRequest.of(0, 1000); // Get all mobilizations
    Page<CrewMobilization> clientMobilizations =
        crewMobilizationQueryUseCase.findMobilizations(criteria, pageable);

    // Collect all assigned crew from all mobilizations
    List<AssignedCrew> allAssignedCrews =
        clientMobilizations.getContent().stream()
            .flatMap(mobilization -> mobilization.getCrews().stream())
            .toList();

    // Get unique employee card IDs
    List<String> allEmployeeCardIds =
        allAssignedCrews.stream()
            .map(AssignedCrew::getEmployeeCardId)
            .distinct()
            .collect(Collectors.toList());

    if (allEmployeeCardIds.isEmpty()) {
      return List.of();
    }

    // Get crew profiles for all employee card IDs
    List<CrewProfile> crewProfiles = crewUseCase.getProfilesByEmployeeCardIds(allEmployeeCardIds);

    return crewProfiles.stream()
        .map(
            crew -> {
              // Find all mobilizations where this crew was assigned
              List<AssignedCrew> crewAssignments =
                  allAssignedCrews.stream()
                      .filter(
                          assignedCrew ->
                              assignedCrew.getEmployeeCardId().equals(crew.getEmployeeCardId()))
                      .toList();

              // Create mobilization history
              List<ClientCrewSearchResponse.ClientAssignmentHistory> assignmentHistory =
                  clientMobilizations.getContent().stream()
                      .filter(
                          mobilization ->
                              mobilization.getCrews().stream()
                                  .anyMatch(
                                      assignedCrew ->
                                          assignedCrew
                                              .getEmployeeCardId()
                                              .equals(crew.getEmployeeCardId())))
                      .sorted(Comparator.comparing(CrewMobilization::getStartDate).reversed())
                      .map(
                          mobilization -> {
                            AssignedCrew assignedCrew =
                                mobilization.getCrews().stream()
                                    .filter(
                                        ac ->
                                            ac.getEmployeeCardId().equals(crew.getEmployeeCardId()))
                                    .findFirst()
                                    .orElse(null);

                            return ClientCrewSearchResponse.ClientAssignmentHistory.builder()
                                .shipScheduleId(mobilization.getId())
                                .shipImo(
                                    mobilization.getShipInfo() != null
                                        ? mobilization.getShipInfo().getImoNumber()
                                        : null)
                                .shipName(
                                    mobilization.getShipInfo() != null
                                        ? mobilization.getShipInfo().getName()
                                        : null)
                                .departureTime(mobilization.getStartDate())
                                .arrivalTime(mobilization.getEndDate())
                                .departurePort(null) // Not available in mobilization
                                .arrivalPort(null) // Not available in mobilization
                                .scheduleStatus(mobilization.getStatus().name())
                                .assignmentDate(mobilization.getCreatedAt())
                                .build();
                          })
                      .collect(Collectors.toList());

              Instant lastAssignmentDate =
                  assignmentHistory.stream()
                      .map(ClientCrewSearchResponse.ClientAssignmentHistory::getAssignmentDate)
                      .max(Comparator.naturalOrder())
                      .orElse(null);

              return ClientCrewSearchResponse.builder()
                  .id(crew.getId())
                  .accountId(crew.getAccountId())
                  .fullName(crew.getFullName())
                  .email(crew.getEmail())
                  .phoneNumber(crew.getPhoneNumber())
                  .image(crew.getImage())
                  .address(crew.getAddress())
                  .gender(crew.getGender())
                  .status(crew.getStatus())
                  .professionalPosition(crew.getProfessionalPosition())
                  .birthDate(crew.getBirthDate())
                  .employeeCardId(crew.getEmployeeCardId())
                  .citizenIdentityCardId(crew.getCitizenIdentityCardId())
                  .citizenIdentityCardImageFront(crew.getCitizenIdentityCardImageFront())
                  .citizenIdentityCardImageBack(crew.getCitizenIdentityCardImageBack())
                  .socialInsuranceCode(crew.getSocialInsuranceCode())
                  .socialInsuranceImageFront(crew.getSocialInsuranceImageFront())
                  .socialInsuranceImageBack(crew.getSocialInsuranceImageBack())
                  .accidentInsuranceCode(crew.getAccidentInsuranceCode())
                  .accidentInsuranceImageFront(crew.getAccidentInsuranceImageFront())
                  .accidentInsuranceImageBack(crew.getAccidentInsuranceImageBack())
                  .assignmentHistory(assignmentHistory)
                  .totalAssignmentsForClient(assignmentHistory.size())
                  .lastAssignmentDate(lastAssignmentDate)
                  .build();
            })
        .sorted(
            Comparator.comparing(
                ClientCrewSearchResponse::getLastAssignmentDate,
                Comparator.nullsLast(Comparator.reverseOrder())))
        .collect(Collectors.toList());
  }
}
