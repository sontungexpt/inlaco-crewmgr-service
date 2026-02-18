package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.create;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCrewSupplyContract extends NewContract {

  public NewCrewSupplyContract() {
    super(ContractType.SUPPLY_CONTRACT);
  }

  private int numOfCrews;

  private String crewRentalRequestId;

  private ShipInfo shipInfo;
}
