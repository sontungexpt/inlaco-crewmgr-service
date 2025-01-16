package com.inlaco.crewmgrservice.feature.contract.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldNameConstants;

@Schema(description = "Party Type")
@FieldNameConstants
public enum PartyType {
  @Schema(description = "Dynamic Party")
  @FieldNameConstants.Include
  DYNAMIC,

  @Schema(description = "Static Party")
  @FieldNameConstants.Include
  STATIC,

  @Schema(description = "Labor Party")
  @FieldNameConstants.Include
  LABOR,
}
