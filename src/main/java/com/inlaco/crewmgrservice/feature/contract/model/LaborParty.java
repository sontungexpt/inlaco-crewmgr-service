package com.inlaco.crewmgrservice.feature.contract.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class LaborParty extends DynamicParty {

  public LaborParty() {
    super(PartyType.LABOR);
  }

  @Schema(description = "The tax code of the contract", example = "123456")
  private String taxCode;

  @Schema(description = "The card id of the user (cccd)", example = "123456")
  @NotBlank
  private String identificationCardId;

  @Schema(description = "The issued date of the card", example = "2022-01-01")
  @Past
  private Instant identificationCardIssuedDate;

  @Schema(description = "The issued place of the card", example = "Hanoi")
  @NotBlank
  private String identificationCardIssuedPlace;

  @Schema(description = "The bank account of the contract", example = "123456")
  @NotBlank
  private String bankAccount;

  @Schema(description = "The bank name of the contract", example = "ACB")
  @NotBlank
  private String bankName;

  @Schema(description = "The birth date of the contract", example = "2022-01-01")
  @Past
  private Instant birthDate;

  @Schema(description = "The birth place of the contract", example = "Hanoi")
  @NotBlank
  private String birthPlace;

  @Schema(description = "The nationality of party")
  @NotBlank
  private String nationality;

  @Schema(description = "The temporary address of party")
  @NotBlank
  private String temporaryAddress;
}
