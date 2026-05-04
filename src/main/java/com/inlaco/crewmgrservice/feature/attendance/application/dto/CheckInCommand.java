package com.inlaco.crewmgrservice.feature.attendance.application.dto;

import lombok.Data;

@Data
public class CheckInCommand {

  private final String qrToken;
  private final String deviceId;
  private final String location;
}
