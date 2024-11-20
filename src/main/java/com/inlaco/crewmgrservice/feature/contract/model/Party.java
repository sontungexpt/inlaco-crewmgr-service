package com.inlaco.crewmgrservice.feature.contract.model;

import com.inlaco.crewmgrservice.common.model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class Party {

  @Schema(
      description = "The name of the party",
      examples = {"Người lao động", "Người sử dụng lao động"})
  @NotBlank
  private String partyName;

  @Schema(description = "The account of the contract", example = "123456")
  private String account;

  @Schema(description = "The employee name of the contract", example = "John Doe")
  private String name;

  @Schema(description = "The employee email of the contract", example = " [email protected]")
  private String email;

  @Schema(description = "The employee phone of the contract", example = "0123456789")
  private String phone;

  @Schema(description = "The address of the contract")
  private Address address;

  @Schema(description = "The represent of the contract", example = "John Doe")
  private String represent;

  @Schema(description = "The tax code of the contract", example = "123456")
  private String taxCode;

  @Schema(description = "The bank account of the contract", example = "123456")
  private String bankAccount;

  @Schema(description = "The bank name of the contract", example = "ACB")
  private String bankName;
}
