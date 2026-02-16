package com.inlaco.crewmgrservice.feature.crew.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.crew.domain.enums.CrewStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class CrewProfileResponse {

  private CrewStatus status;

  private String professionalPosition;

  private Instant birthDate;

  private String employeeCardId;

  @Deprecated
  public String getCardId() {
    return employeeCardId;
  }

  private String socialInsuranceCode;

  private List<Asset> socialInsuranceImages;

  private String accidentInsuranceCode;

  private List<Asset> accidentInsuranceImages;
}
