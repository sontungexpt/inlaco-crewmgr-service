package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import lombok.experimental.SuperBuilder;

@JsonTypeName(ContractType.Fields.SUPPLY_CONTRACT)
@SuperBuilder
public class SupplyContract extends DynamicContract {}
