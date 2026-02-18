package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.contract;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@Getter
@Setter
public class CrewSupplyContractEntity extends ContractEntity {

  public CrewSupplyContractEntity() {
    super(ContractType.SUPPLY_CONTRACT);
  }

  private int numOfCrews;
  private ObjectId crewRentalRequestId;
  private ShipInfo shipInfo;
}
