package com.inlaco.crewmgrservice.feature.crewrental.presentation.dto;

import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.AssetResponse;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfoResponse;
import java.time.Instant;
import lombok.Data;

@Data
public class CrewRentalRequestResponse {

  private String id;

  private AssetResponse detailFile;

  private String companyName;

  private String companyAddress;

  private String companyPhone;

  private String companyEmail;

  private String companyRepresentor;

  private String companyRepresentorPosition;

  private Instant rentalStartDate;

  private Instant rentalEndDate;

  private ShipInfoResponse shipInfo;

  private String contractId;

  public boolean hasContract() {
    return contractId != null;
  }

  private CrewRentalRequestStatus status = CrewRentalRequestStatus.PENDING;
}
