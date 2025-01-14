package com.inlaco.crewmgrservice.feature.contract.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.experimental.FieldNameConstants;

@Schema(description = "Defines the type of contract available.")
@FieldNameConstants
public enum ContractType {
  @FieldNameConstants.Include
  @Schema(description = "A contract for the supply of dynamic goods or services.")
  DYNAMIC_CONTRACT,
  @FieldNameConstants.Include
  @Schema(description = "A contract for the supply of goods or services.")
  SUPPLY_CONTRACT,
  @FieldNameConstants.Include
  @Schema(description = "A contract for employment or labor.")
  LABOR_CONTRACT;
}
