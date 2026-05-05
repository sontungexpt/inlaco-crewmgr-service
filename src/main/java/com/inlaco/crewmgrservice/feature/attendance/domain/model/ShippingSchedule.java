package com.inlaco.crewmgrservice.feature.attendance.domain.model;

import java.time.Instant;
import java.util.List;
import lombok.Data;

@Data
public class ShippingSchedule {
  private String id;

  private String clientId;
  private String shipId;

  private Instant departureTime;

  private List<String> employeeCardIds; // include ALL sailors
}
