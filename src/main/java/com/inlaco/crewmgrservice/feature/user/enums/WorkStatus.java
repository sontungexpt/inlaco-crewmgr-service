package com.inlaco.crewmgrservice.feature.user.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The status of the work")
public enum WorkStatus {
  AVAILABLE,

  ASSIGNED,

  ON_LEAVE,

  OFF_DUTY,
}
