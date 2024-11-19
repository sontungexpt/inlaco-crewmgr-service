package com.inlaco.crewmgrservice.feature.notify;

import io.swagger.v3.oas.annotations.media.Schema;

public final class NotificationType {
  @Schema(description = "SMS notification type")
  public static final String SMS = "SMS";

  @Schema(description = "Email notification type")
  public static final String EMAIL = "EMAIL";
}
