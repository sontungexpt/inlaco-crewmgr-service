package com.inlaco.crewmgrservice.feature.crew.domain.model;

import com.inlaco.crewmgrservice.common.model.Asset;
import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewStatus;
import com.inlaco.crewmgrservice.feature.user.domain.enums.Gender;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class CrewProfile {

  private String id;

  private String accountId;

  private String fullName;

  private String email;

  private String phoneNumber;

  private String address;

  private Gender gender;

  private CrewStatus status = CrewStatus.DRAFT;

  public void changeStatus(CrewStatus newStatus) throws IllegalStateException {
    if (status == newStatus) return;
    status.validateTransition(newStatus);
    status = newStatus;
  }

  private String professionalPosition;

  private Instant birthDate;

  private String employeeCardId;

  private String socialInsuranceCode;

  private List<Asset> socialInsuranceImages;

  private String accidentInsuranceCode;

  private List<Asset> accidentInsuranceImages;
}
