package com.inlaco.crewmgrservice.feature.contract.domain.model.party;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.PartyType;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Party {

  private String accountId;

  private PartyType type;

  public Party(PartyType type) {
    this.type = type;
  }

  private String partyName;

  private String representer;
  private String representerPosition;

  private String email;

  private String phone;

  private String address;
}
