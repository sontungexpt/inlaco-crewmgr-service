package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;

@SuperBuilder
@Getter
@JsonTypeName(ContractType.Fields.SUPPLY_CONTRACT)
@Setter
public class SupplyContract extends DynamicContract {

  @JsonIgnore
  @Schema(description = "The rental request id that this contract is created from", hidden = true)
  private ObjectId rentalRequestId;
}
