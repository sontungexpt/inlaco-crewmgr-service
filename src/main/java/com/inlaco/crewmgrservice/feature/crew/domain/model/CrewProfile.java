package com.inlaco.crewmgrservice.feature.crew.domain.model;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewStatus;
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
public class CrewProfile {

  private String id;

  private String accountId;

  private String fullName;

  private String email;

  private String phoneNumber;

  private String address;

  private Gender gender;

  @Builder.Default private CrewStatus status = CrewStatus.DRAFT;

  public void changeStatus(CrewStatus newStatus) throws IllegalStateException {
    if (status == newStatus) return;
    status.validateTransition(newStatus);
    status = newStatus;
  }

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
}
