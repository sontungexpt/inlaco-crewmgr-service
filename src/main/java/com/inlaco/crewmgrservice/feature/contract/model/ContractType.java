package com.inlaco.crewmgrservice.feature.contract.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Defines the type of contract available.")
public enum ContractType {
  @Schema(description = "A contract for the supply of goods or services.")
  SUPPLY_CONTRACT,

  @Schema(description = "A contract for employment or labor.")
  LABOR_CONTRACT
}
