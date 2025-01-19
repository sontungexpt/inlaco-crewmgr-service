package com.inlaco.crewmgrservice.feature.contract.model;

import com.inlaco.crewmgrservice.common.model.DynamicAttribute;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class DynamicParty extends Party {

  public DynamicParty() {
    super(PartyType.DYNAMIC);
  }

  public DynamicParty(PartyType type) {
    super(type);
  }

  private List<DynamicAttribute> customAttributes;
}
