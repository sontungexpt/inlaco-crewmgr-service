package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.party;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.PartyType;
import com.inlaco.crewmgrservice.feature.contract.domain.model.party.Party;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.DynamicAttribute;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DynamicParty extends Party {

  public DynamicParty() {
    super(PartyType.DYNAMIC);
  }

  public DynamicParty(PartyType type) {
    super(type);
  }

  private List<DynamicAttribute> customAttributes;
}
