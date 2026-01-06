package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.inlaco.crewmgrservice.validation.annotation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;

@Getter
@Setter
@SuperBuilder
@JsonTypeInfo(
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true,
    use = JsonTypeInfo.Id.NAME,
    defaultImpl = DynamicParty.class,
    property = "type")
@JsonSubTypes({
  @JsonSubTypes.Type(value = Party.class, name = PartyType.Fields.STATIC),
  @JsonSubTypes.Type(value = DynamicParty.class, name = PartyType.Fields.DYNAMIC),
  @JsonSubTypes.Type(value = LaborParty.class, name = PartyType.Fields.LABOR),
})
@CompoundIndexes({
  @CompoundIndex(name = "party_search_index", def = "{'phone': 1, 'email': 1, 'representer': 1}")
})
@NoArgsConstructor
public class Party {

  public Party(PartyType type) {
    this.type = type;
  }

  @Schema(
      description = "The name of the party",
      examples = {"Người lao động", "Người sử dụng lao động"})
  @NotBlank
  private String partyName;

  @Schema(description = "The employee name of the contract", example = "John Doe")
  private String representer;

  @Schema(description = "The employee position of the contract", example = "Engineer")
  private String representerPosition;

  @Schema(description = "The employee email of the contract", example = " [email protected]")
  @Email
  private String email;

  @PhoneNumber
  @Schema(description = "The employee phone of the contract", example = "0392211343")
  private String phone;

  @Schema(description = "The address of the contract")
  private String address;

  @Schema(description = "The account id of the party", example = "5f9b1b7b7f7b7b7b7b7b7b7b")
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId accountId;

  @NotNull
  @Schema(description = "The type of the party", example = "STATIC")
  private PartyType type;
}
