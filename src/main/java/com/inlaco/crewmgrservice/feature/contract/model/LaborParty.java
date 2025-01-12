package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.Instant;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@JsonTypeName(PartyType.Fields.DYNAMIC)
public class LaborParty extends DynamicParty {

  @Schema(description = "The account of the contract", example = "123456")
  private String account;

  @Schema(description = "The tax code of the contract", example = "123456")
  private String taxCode;

  @Schema(description = "The card id of the user (cccd)", example = "123456")
  @NotBlank
  private String identificationCardId;

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
}
