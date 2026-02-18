package com.inlaco.crewmgrservice.feature.contract.domain.model.party;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.PartyType;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaborParty extends Party {

  public LaborParty() {
    super(PartyType.LABOR);
  }

  private String taxCode;

  private String identificationCardId;
  private Instant identificationCardIssuedDate;
  private String identificationCardIssuedPlace;

  private String bankAccount;
  private String bankName;

  private Instant birthDate;
  private String birthPlace;

  private String nationality;

  private String temporaryAddress;
}
