package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

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
@CompoundIndexes({
  @CompoundIndex(name = "party_search_index", def = "{'phone': 1, 'email': 1, 'representer': 1}")
})
public class Party {

  @Schema(
      description = "The name of the party",
      examples = {"Người lao động", "Người sử dụng lao động"})
  @NotBlank
  private String partyName;

  @Schema(description = "The employee name of the contract", example = "John Doe")
  private String representer;

  @Schema(description = "The employee email of the contract", example = " [email protected]")
  private String email;

  @Schema(description = "The employee phone of the contract", example = "0123456789")
  private String phone;

  @Schema(description = "The address of the contract")
  private String address;

  // @Schema(description = "The account id of the party", example = "5f9b1b7b7f7b7b7b7b7b7b7b")
  // @Indexed
  // private ObjectId accountId;

  @Default
  @Schema(description = "The type of the party", example = "STATIC")
  @NotNull
  private PartyType type = PartyType.STATIC;
}
