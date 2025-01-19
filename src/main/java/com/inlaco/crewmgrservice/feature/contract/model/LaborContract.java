package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@Getter
@Setter
@SuperBuilder
public class LaborContract extends DynamicContract {

  public LaborContract() {
    super(ContractType.LABOR_CONTRACT);
  }

  @Schema(description = "Employee ID", hidden = true)
  @JsonIgnore
  @JsonPatchIgnore
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId employeeId;
}
