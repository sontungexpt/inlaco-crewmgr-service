package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.common.model.ShipInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
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

  @Schema(description = "The number of crews")
  @JsonPatchIgnore
  @Min(1)
  private int numOfCrews;

  @Schema(description = "The rental request id that this contract is created from")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonPatchIgnore
  private ObjectId rentalRequestId;

  @Schema(description = "Ship information.")
  private ShipInfo shipInfo;
}
