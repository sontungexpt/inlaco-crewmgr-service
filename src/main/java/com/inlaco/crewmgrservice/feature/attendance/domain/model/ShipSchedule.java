package com.inlaco.crewmgrservice.feature.attendance.domain.model;

import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class ShipSchedule {
  private String id;

  private String createdBy;
  private String shipIMO;

  private Instant departureTime;
  private Instant estimatedArrivalTime;

  private List<String> employeeCardIds; // include ALL sailors
}
