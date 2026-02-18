package com.inlaco.crewmgrservice.feature.contract.domain.model.party;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.PartyType;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.DynamicAttribute;
import java.util.List;
import lombok.Data;

@Data
public class Party {
  private String name;

  private final PartyType type;

  protected Party(PartyType type) {
    this.type = type;
  }

  public Party() {
    this.type = PartyType.STATIC;
  }

  private String representer;
  private String representerPosition;

  private String email;
  private String phone;
  private String address;

  private List<DynamicAttribute> customAttributes;

  public String getPartyName() {
    return name;
  }
}
