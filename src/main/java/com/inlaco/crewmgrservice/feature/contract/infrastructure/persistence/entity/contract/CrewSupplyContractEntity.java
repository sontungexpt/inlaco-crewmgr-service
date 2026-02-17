package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.contract;

import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CrewSupplyContractEntity extends AbstractContractEntity {

  private int numOfCrews;
  private String crewRentalRequestId;
  private ShipInfo shipInfo;
}
