package com.inlaco.crewmgrservice.feature.contract.domain.model;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateCrewSupplyContractCommand extends UpdateContractCommand {
  Patch<Integer> numOfCrews;
  Patch<ShipInfo> shipInfo;
}
