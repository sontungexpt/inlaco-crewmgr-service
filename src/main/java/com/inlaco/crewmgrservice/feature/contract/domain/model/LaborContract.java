package com.inlaco.crewmgrservice.feature.contract.domain.model;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaborContract extends AbstractContract {

  public LaborContract() {
    super(ContractType.LABOR_CONTRACT);
  }

  private String accountId;

  private String applicationId;

  private String position;

  private String workingLocation;

  private String basicSalary;

  private String allowance;

  private String receiveMethod;

  private String payday;

  private String salaryReviewPeriod;
}
