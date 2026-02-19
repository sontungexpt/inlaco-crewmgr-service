package com.inlaco.crewmgrservice.feature.contract.domain.model;

import com.inlaco.crewmgrservice.shared.application.model.Patch;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class UpdateLaborContractCommand extends UpdateContractCommand {
  Patch<String> position;
  Patch<String> workingLocation;
  Patch<String> basicSalary;
  Patch<String> allowance;
  Patch<String> receiveMethod;
  Patch<String> payday;
  Patch<String> salaryReviewPeriod;
}
