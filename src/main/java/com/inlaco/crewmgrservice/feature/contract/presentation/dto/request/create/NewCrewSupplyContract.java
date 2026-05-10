package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.create;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewCrewSupplyContract extends NewContract {

  public NewCrewSupplyContract() {
    super(ContractType.SUPPLY_CONTRACT);
  }

  @NotNull private ShipInfoRequest shipInfo;
}
