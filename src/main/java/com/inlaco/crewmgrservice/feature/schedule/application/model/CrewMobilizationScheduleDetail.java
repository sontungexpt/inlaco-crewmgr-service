package com.inlaco.crewmgrservice.feature.schedule.application.model;

import com.inlaco.crewmgrservice.common.model.ShipInfo;
import com.inlaco.crewmgrservice.feature.schedule.domain.enums.CrewMobilizationScheduleStatus;
import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class CrewMobilizationScheduleDetail {

  private String id;
  private String partnerName;
  private String partnerPhone;
  private String partnerEmail;
  private String partnerAddress;
  private ShipInfo shipInfo;
  private Instant startDate;
  private Instant endDate;
  private CrewMobilizationScheduleStatus status;
  private List<AssignedCrewDetail> crews;
  private Instant createdAt;
  private Instant updatedAt;
}
