package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.party;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.PartyType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Past;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

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

  private String bankAccount;

  private String bankName;

  private Instant birthDate;

  private String birthPlace;

  private String nationality;

  private String temporaryAddress;
}
