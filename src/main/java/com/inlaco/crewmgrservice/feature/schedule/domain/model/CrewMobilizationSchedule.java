package com.inlaco.crewmgrservice.feature.schedule.domain.model;

import com.inlaco.crewmgrservice.feature.schedule.domain.enums.CrewMobilizationScheduleStatus;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import java.time.Instant;
import java.util.Set;
import lombok.Data;

@Data
public class CrewMobilizationSchedule {

  private String id;

  private String partnerName;

  private String partnerPhone;

  private String partnerEmail;

  private String partnerAddress;

  private ShipInfo shipInfo;

  private Instant startDate;

  private Instant endDate;

  private CrewMobilizationScheduleStatus status;

  private Set<AssignedCrew> crews;

  public int getCrewNumbers() {
    return crews == null ? 0 : crews.size();
  }

  private Instant createdAt;

  private Instant updatedAt;
}
