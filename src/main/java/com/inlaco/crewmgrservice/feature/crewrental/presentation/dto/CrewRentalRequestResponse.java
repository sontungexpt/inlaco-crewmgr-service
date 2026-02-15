package com.inlaco.crewmgrservice.feature.crewrental.presentation.dto;

import com.inlaco.crewmgrservice.common.model.Asset;
import com.inlaco.crewmgrservice.common.model.ShipInfo;
import com.inlaco.crewmgrservice.feature.crewrental.domain.enums.CrewRentalRequestStatus;
import java.time.Instant;
import lombok.Data;

@Data
public class CrewRentalRequestResponse {

  private String id;

  private Asset detailFile;

  private String companyName;

  private String companyAddress;

  private String companyPhone;

  private String companyEmail;

  private String companyRepresentor;

  private String companyRepresentorPosition;

  private Instant rentalStartDate;

  private Instant rentalEndDate;

  private ShipInfo shipInfo;

  private String contractId;

  public boolean hasContract() {
    return contractId != null;
  }

  private CrewRentalRequestStatus status = CrewRentalRequestStatus.PENDING;
}
