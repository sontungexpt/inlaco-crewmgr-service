package com.inlaco.crewmgrservice.feature.shipschedule.application.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class QrVerifyCommand {

  private String token;

  private String deviceId;

  private String location;
}
