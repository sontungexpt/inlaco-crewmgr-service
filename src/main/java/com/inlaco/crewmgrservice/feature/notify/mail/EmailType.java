package com.inlaco.crewmgrservice.feature.notify.mail;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Email type")
public enum EmailType {
  @Schema(description = "Simple email type")
  SIMPLE,

  @Schema(description = "HTML email type")
  HTML,
}
