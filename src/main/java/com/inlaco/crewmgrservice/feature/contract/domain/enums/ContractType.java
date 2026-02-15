package com.inlaco.crewmgrservice.feature.contract.domain.enums;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public enum ContractType {
  @FieldNameConstants.Include
  SUPPLY_CONTRACT,

  @FieldNameConstants.Include
  LABOR_CONTRACT;
}
