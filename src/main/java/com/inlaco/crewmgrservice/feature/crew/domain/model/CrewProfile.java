package com.inlaco.crewmgrservice.feature.crew.domain.model;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.BoardingStatus;
import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewOperationalStatus;
import com.inlaco.crewmgrservice.feature.crew.domain.enums.MobilizationStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CrewProfile {

  private String id;

  private String accountId;

  private String fullName;

  private String email;

  private String phoneNumber;

  private Asset image;

  private String address;

  private Gender gender;

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

  @Builder.Default private CrewOperationalStatus status = CrewOperationalStatus.DRAFT;

  public void changeStatus(CrewOperationalStatus newStatus) throws IllegalStateException {
    if (status == newStatus) return;
    status.validateTransition(newStatus);
    status = newStatus;
  }

  @Builder.Default private BoardingStatus boardingStatus = BoardingStatus.OFF_BOARD;

  public void board() {
    boardingStatus = BoardingStatus.ON_BOARD;
  }

  @Builder.Default private MobilizationStatus mobilizationStatus = MobilizationStatus.AVAILABLE;

  public void mobilize() {
    mobilizationStatus = MobilizationStatus.MOBILIZED;
  }
}
