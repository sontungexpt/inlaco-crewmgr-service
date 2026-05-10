package com.inlaco.crewmgrservice.feature.contract.domain.model;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaborContract extends Contract {

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

  public void validateForSigning() {
    super.validateForSigning();

    ContractValidator.notBlank(accountId, "Account id is required");
    ContractValidator.notBlank(applicationId, "Application id is required");
    ContractValidator.notBlank(position, "Position is required");
    ContractValidator.notBlank(workingLocation, "Working location is required");
    ContractValidator.notBlank(basicSalary, "Basic salary is required");
    ContractValidator.notBlank(allowance, "Allowance is required");
    ContractValidator.notBlank(receiveMethod, "Receive method is required");
    ContractValidator.notBlank(payday, "Payday is required");
    ContractValidator.notBlank(salaryReviewPeriod, "Salary review period is required");
  }
}
