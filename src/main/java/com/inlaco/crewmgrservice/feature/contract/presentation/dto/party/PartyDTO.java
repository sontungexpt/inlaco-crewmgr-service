package com.inlaco.crewmgrservice.feature.contract.presentation.dto.party;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.PartyType;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.DynamicAttribute;
import jakarta.validation.constraints.NotBlank;
import java.util.List;
import lombok.Data;

@Data
@JsonTypeInfo(
    include = JsonTypeInfo.As.EXISTING_PROPERTY,
    visible = true,
    use = JsonTypeInfo.Id.NAME,
    property = "type")
@JsonSubTypes({
  @Type(value = LaborPartyDTO.class, name = PartyType.Fields.LABOR),
  @Type(value = PartyDTO.class, name = PartyType.Fields.STATIC),
})
public class PartyDTO {

  private final PartyType type;

  protected PartyDTO(PartyType type) {
    this.type = type;
  }

  public PartyDTO() {
    this.type = PartyType.STATIC;
  }

  @JsonAlias({"partyName", "name"})
  @NotBlank
  private String name;

  public String getPartyName() {
    return name;
  }

  @NotBlank private String representer;
  @NotBlank private String representerPosition;

  private String email;

  private String phone;

  private String address;

  private List<DynamicAttribute> customAttributes;
}
