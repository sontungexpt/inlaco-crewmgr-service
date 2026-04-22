package com.inlaco.crewmgrservice.feature.notify.domain.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Email type")
public enum EmailType {
  @Schema(description = "Simple email type")
  TEXT,

  @Schema(description = "Mime email type (HTML, with attachments, ...)")
  MIME,
}
