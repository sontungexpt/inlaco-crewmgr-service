package com.inlaco.crewmgrservice.feature.crewmobilization.domain.model;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.enums.CrewMobilizationStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import java.time.Instant;
import lombok.Data;

@Data
public class CrewMobilization {

  private String id;

  private String contractId;
  private String crewRentalRequestId;

  private String partnerAccountId;

  private String partnerName;

  private String partnerPhone;

  private String partnerEmail;

  private String partnerAddress;

  private ShipInfo shipInfo;

  private Instant startDate;

  private Instant endDate;

  private CrewMobilizationStatus status = CrewMobilizationStatus.ACTIVE;

  private Instant createdAt;

  private Instant updatedAt;
}
