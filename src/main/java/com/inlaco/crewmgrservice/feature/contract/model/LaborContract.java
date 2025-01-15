package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
@JsonTypeName(ContractType.Fields.LABOR_CONTRACT)
public class LaborContract extends DynamicContract {

  @Schema(description = "Employee ID", hidden = true)
  @JsonIgnore
  @JsonPatchIgnore
  private ObjectId employeeId;
}
