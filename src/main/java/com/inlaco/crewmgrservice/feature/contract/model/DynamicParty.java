package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonTypeName;
import com.inlaco.crewmgrservice.common.model.DynamicAttribute;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@JsonTypeName(PartyType.Fields.DYNAMIC)
public class DynamicParty extends Party {

  private List<DynamicAttribute> customAttributes;
}
