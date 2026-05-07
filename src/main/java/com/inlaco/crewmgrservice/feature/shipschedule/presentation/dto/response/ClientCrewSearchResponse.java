package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ClientCrewSearchResponse {
  
  private String id;
  private String accountId;
  private String fullName;
  private String email;
  private String phoneNumber;
  private Asset image;
  private String address;
  private Gender gender;
  private CrewStatus status;
  private String professionalPosition;
  private Instant birthDate;
  private String employeeCardId;
  private String citizenIdentityCardId;
  private Asset citizenIdentityCardImageFront;
  private Asset citizenIdentityCardImageBack;
  private String socialInsuranceCode;
  private Asset socialInsuranceImageFront;
  private Asset socialInsuranceImageBack;
  private String accidentInsuranceCode;
  private Asset accidentInsuranceImageFront;
  private Asset accidentInsuranceImageBack;
  
  // Assignment history for this client
  private List<ClientAssignmentHistory> assignmentHistory;
  private int totalAssignmentsForClient;
  private Instant lastAssignmentDate;
  
  @Data
  @Builder
  @AllArgsConstructor
  @NoArgsConstructor
  public static class ClientAssignmentHistory {
    private String shipScheduleId;
    private String shipImo;
    private String shipName;
    private Instant departureTime;
    private Instant arrivalTime;
    private String departurePort;
    private String arrivalPort;
    private String scheduleStatus;
    private Instant assignmentDate;
  }
}
