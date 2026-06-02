package com.inlaco.crewmgrservice.feature.crewmobilization.application.model;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.enums.CrewMobilizationStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class CrewMobilizationDetail {

  private String id;
  private String partnerName;
  private String partnerPhone;
  private String partnerEmail;
  private String partnerAddress;
  private ShipInfo shipInfo;
  private Instant startDate;
  private Instant endDate;
  private CrewMobilizationStatus status;
  private List<AssignedCrewDetail> crews;
  private Instant createdAt;
  private Instant updatedAt;
}
