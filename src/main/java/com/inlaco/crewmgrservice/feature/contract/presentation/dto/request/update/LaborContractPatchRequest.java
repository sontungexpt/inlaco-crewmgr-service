package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.update;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class LaborContractPatchRequest extends ContractPatchRequest {
  Patch<String> position = Patch.unchanged();
  Patch<String> workingLocation = Patch.unchanged();
  Patch<String> basicSalary = Patch.unchanged();
  Patch<String> allowance = Patch.unchanged();
  Patch<String> receiveMethod = Patch.unchanged();
  Patch<String> payday = Patch.unchanged();
  Patch<String> salaryReviewPeriod = Patch.unchanged();
}
