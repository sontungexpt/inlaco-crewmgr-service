package com.inlaco.crewmgrservice.feature.crew.presentation.dto.request;

import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class NewCrewProfile {

  private String professionalPosition;

  private Instant birthDate;

  // @JsonAlias({"cardId", "employeeCardId"})
  // private String employeeCardId;

  private String socialInsuranceCode;

  private List<Asset> socialInsuranceImages;

  private String accidentInsuranceCode;

  private List<Asset> accidentInsuranceImages;
}
