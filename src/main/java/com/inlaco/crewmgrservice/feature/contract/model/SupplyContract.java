package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.common.model.ShipInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Setter
@SuperBuilder
public class SupplyContract extends DynamicContract {

  public SupplyContract() {
    super(ContractType.SUPPLY_CONTRACT);
  }

  @Schema(description = "The number of crews")
  @JsonPatchIgnore
  private int numOfCrews;

  @Schema(description = "The rental request id that this contract is created from")
  @JsonSerialize(using = ToStringSerializer.class)
  @JsonPatchIgnore
  private ObjectId rentalRequestId;

  // Planned schedule information
  @Schema(
      description = "Rental start date.",
      example = "2025-01-14T10:00:00Z",
      requiredMode = RequiredMode.REQUIRED)
  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Future
  private Instant rentalStartDate;

  @Schema(
      description = "Rental end date.",
      example = "2025-01-20T18:00:00Z",
      requiredMode = RequiredMode.REQUIRED)
  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Future
  private Instant rentalEndDate;

  @Schema(description = "Ship information.")
  private ShipInfo shipInfo;
}
