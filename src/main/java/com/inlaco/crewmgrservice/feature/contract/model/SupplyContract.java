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
public class SupplyContract extends DynamicContract {

  public SupplyContract() {
    super(ContractType.SUPPLY_CONTRACT);
  }

  @JsonIgnore
  @Schema(description = "The rental request id that this contract is created from", hidden = true)
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonPatchIgnore
  private ObjectId rentalRequestId;
}
