package com.inlaco.crewmgrservice.feature.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class SailorPositionResponse {

  @Schema(
      description = "The name of the sailor position",
      example = "Captain",
      requiredMode = RequiredMode.REQUIRED)
  private String name;

  @Schema(
      description = "The description of the sailor position",
      example = "The captain of the ship.",
      requiredMode = RequiredMode.REQUIRED)
  private String description;
}
