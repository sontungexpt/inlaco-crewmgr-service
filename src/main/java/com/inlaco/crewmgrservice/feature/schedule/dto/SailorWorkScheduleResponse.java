package com.inlaco.crewmgrservice.feature.schedule.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@AllArgsConstructor
@Getter
@Setter
public class SailorWorkScheduleResponse {

  @Id
  @Schema(
      description = "The ID of the sailor work schedule",
      type = "String",
      example = "5f7f1b3b7f4b7b001f3b3b7f")
  private String id;

  @Schema(
      description = "The ID of the sailor that this work schedule is associated with",
      type = "String",
      example = "5f7f1b3b7f4b7b001f3b3b7f")
  private String sailorId;

  @Schema(
      description = "The position of the sailor",
      type = "String",
      requiredMode = RequiredMode.REQUIRED)
  private String professionalPosition;

  // @Schema(description = "The ID of the master assignment schedule", hidden = true)
  // private MasterAssignementScheduleResponse detail;
}
