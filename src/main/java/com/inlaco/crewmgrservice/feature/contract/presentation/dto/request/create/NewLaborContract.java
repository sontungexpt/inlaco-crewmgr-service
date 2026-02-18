package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request.create;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NewLaborContract extends NewContract {

  public NewLaborContract() {
    super(ContractType.LABOR_CONTRACT);
  }

  private String position;

  private String workingLocation;

  private String basicSalary;

  private String allowance;

  private String receiveMethod;

  private String payday;

  private String salaryReviewPeriod;
}
