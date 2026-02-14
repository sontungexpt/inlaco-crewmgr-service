package com.inlaco.crewmgrservice.feature.contract.presentation.dto.response;

import com.inlaco.crewmgrservice.common.model.ShipInfo;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrewSupplyContractResponse extends DynamicContractResponse {

  public CrewSupplyContractResponse() {
    super(ContractType.SUPPLY_CONTRACT);
  }

  private int numOfCrews;

  private String crewRentalRequestId;

  private ShipInfo shipInfo;
}
