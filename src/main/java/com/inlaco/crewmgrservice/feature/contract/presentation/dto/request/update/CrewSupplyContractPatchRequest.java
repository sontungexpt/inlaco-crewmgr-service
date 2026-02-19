package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.update;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import com.inlaco.crewmgrservice.shared.objectvalue.ShipInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class CrewSupplyContractPatchRequest extends ContractPatchRequest {
  Patch<Integer> numOfCrews = Patch.unchanged();
  Patch<ShipInfo> shipInfo = Patch.unchanged();
}
