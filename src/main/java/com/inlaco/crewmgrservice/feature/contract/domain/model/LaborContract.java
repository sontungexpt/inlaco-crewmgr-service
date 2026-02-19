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

  @Override
  protected <T extends UpdateContractCommand> boolean applyChanges(T command) {
    boolean changed = super.applyChanges(command);
    if (!(command instanceof UpdateLaborContractCommand laborCommand)) return changed;

    return changed
        | laborCommand.getPosition().ifUpdated(this::setPosition)
        | laborCommand.getWorkingLocation().ifUpdated(this::setWorkingLocation)
        | laborCommand.getBasicSalary().ifUpdated(this::setBasicSalary)
        | laborCommand.getAllowance().ifUpdated(this::setAllowance)
        | laborCommand.getReceiveMethod().ifUpdated(this::setReceiveMethod)
        | laborCommand.getPayday().ifUpdated(this::setPayday)
        | laborCommand.getSalaryReviewPeriod().ifUpdated(this::setSalaryReviewPeriod);
  }
}
