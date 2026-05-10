package com.inlaco.crewmgrservice.feature.crew.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.BoardingStatus;
import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewOperationalStatus;
import com.inlaco.crewmgrservice.feature.crew.domain.enums.MobilizationStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.AssetResponse;
import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import java.time.Instant;
import lombok.Data;

@Data
public class CrewProfileResponse {

  private String id;

  private CrewOperationalStatus status;

  private MobilizationStatus mobilizationStatus;

  private BoardingStatus boardingStatus;

  private String professionalPosition;

  private Instant birthDate;

  private String fullName;

  private String email;

  private String phoneNumber;

  private AssetResponse image;

  private String address;

  private Gender gender;

  private String employeeCardId;

  private String citizenIdentityCardId;

  private AssetResponse citizenIdentityCardImageFront;
  private AssetResponse citizenIdentityCardImageBack;

  private String socialInsuranceCode;
  private AssetResponse socialInsuranceImageFront;
  private AssetResponse socialInsuranceImageBack;

  private String accidentInsuranceCode;
  private AssetResponse accidentInsuranceImageFront;
  private AssetResponse accidentInsuranceImageBack;
}
