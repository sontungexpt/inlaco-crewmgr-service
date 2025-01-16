package com.inlaco.crewmgrservice.feature.crewrental.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Status of the request.")
public enum RentalRequestStatus {
  @Schema(description = "The request is pending approval.")
  PENDING,
  @Schema(description = "The request has been approved.")
  APPROVED,
  @Schema(description = "The request is on contract signing stage.")
  SIGNING,
  @Schema(description = "The request is active.")
  ACTIVE,
  @Schema(description = "The request is completed.")
  DONE,
  @Schema(description = "The request has been rejected.")
  REJECTED
}
