package com.inlaco.crewmgrservice.feature.contract.model;

import com.esotericsoftware.kryo.serializers.FieldSerializer.NotNull;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.inlaco.crewmgrservice.validation.annotation.ObjectId;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonTypeInfo(
    include = JsonTypeInfo.As.PROPERTY,
    visible = true,
    use = JsonTypeInfo.Id.NAME,
    property = "type",
    defaultImpl = DynamicParty.class)
@JsonTypeName(PartyType.Fields.STATIC)
public class Party {

  @Schema(
      description = "The name of the party",
      examples = {"Người lao động", "Người sử dụng lao động"})
  @NotBlank
  private String partyName;

  @Schema(description = "The employee name of the contract", example = "John Doe")
  private String fullName;

  @Schema(description = "The employee email of the contract", example = " [email protected]")
  private String email;

  @Schema(description = "The employee phone of the contract", example = "0123456789")
  private String phone;

  @Schema(description = "The address of the contract")
  private String address;

  @Schema(description = "The represent of the contract", example = "John Doe")
  private String represent;

  @Schema(description = "The account id of the party", example = "5f9b1b7b7f7b7b7b7b7b7b7b")
  private ObjectId accountId;

  @Default
  @Schema(description = "The type of the party", example = "STATIC")
  @NotNull
  private PartyType type = PartyType.STATIC;
}
