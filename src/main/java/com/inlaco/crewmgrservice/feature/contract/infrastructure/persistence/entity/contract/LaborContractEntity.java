package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.contract;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LaborContractEntity extends AbstractContractEntity {

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
