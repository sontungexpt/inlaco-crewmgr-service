package com.inlaco.crewmgrservice.feature.contract.infrastructure.persistence.entity.contract;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.index.CompoundIndex;

@CompoundIndex(
    name = "idx_meta_application_status",
    def = "{'applicationId':1, 'status':1}",
    sparse = true)
@Getter
@Setter
public class LaborContractEntity extends ContractEntity {

  public LaborContractEntity() {
    super(ContractType.LABOR_CONTRACT);
  }

  private ObjectId accountId;
  private ObjectId applicationId;
  private String position;
  private String workingLocation;
  private String basicSalary;
  private String allowance;
  private String receiveMethod;
  private String payday;
  private String salaryReviewPeriod;
}
