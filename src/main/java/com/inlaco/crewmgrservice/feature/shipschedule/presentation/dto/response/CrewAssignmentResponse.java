package com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewOperationalStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CrewAssignmentResponse {

  private String id;
  private String accountId;
  private String fullName;
  private String email;
  private String phoneNumber;
  private Asset image;
  private String address;
  private Gender gender;
  private CrewOperationalStatus status;
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

  // Assignment specific fields
  private String shipScheduleId;
  private String shipImo;
  private String shipName;
  private Instant departureTime;
  private Instant arrivalTime;
  private String departurePort;
  private String arrivalPort;
}
