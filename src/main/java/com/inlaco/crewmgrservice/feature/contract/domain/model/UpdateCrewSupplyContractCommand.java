package com.inlaco.crewmgrservice.feature.contract.domain.model;

import com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.update.ShipInfoPatchRequest;
import com.inlaco.crewmgrservice.shared.application.model.Patch;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateCrewSupplyContractCommand extends UpdateContractCommand {
  Patch<ShipInfoPatchRequest> shipInfo;
}
