package com.inlaco.crewmgrservice.feature.crewmobilization.presentation.rest.dto.response;

import com.inlaco.crewmgrservice.feature.crewmobilization.domain.enums.CrewMobilizationStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class CrewMobilizationResponse {

  private String id;

  private CrewMobilizationStatus status;

  // Partner Information
  private String partnerName;

  private String partnerPhone;

  private String partnerEmail;

  private String partnerAddress;

  private ShipInfo shipInfo;

  private Instant startDate;

  private Instant endDate;

  private List<AssignedCrewResponse> crews;

  public int getCrewNumbers() {
    return crews == null ? 0 : crews.size();
  }

  private Instant createdAt;

  private Instant updatedAt;
}
