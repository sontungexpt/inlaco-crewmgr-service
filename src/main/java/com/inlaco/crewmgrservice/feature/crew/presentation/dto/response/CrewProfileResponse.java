package com.inlaco.crewmgrservice.feature.crew.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import com.inlaco.crewmgrservice.shared.objectvalue.Gender;
import java.time.Instant;
import lombok.Data;

@Data
public class CrewProfileResponse {

  private String id;

  private CrewStatus status;

  private String professionalPosition;

  private Instant birthDate;

  private String fullName;

  private String email;

  private String phoneNumber;

  private String address;

  private Gender gender;

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
