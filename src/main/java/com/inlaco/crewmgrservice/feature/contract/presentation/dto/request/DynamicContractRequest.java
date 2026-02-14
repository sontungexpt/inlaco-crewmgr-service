package com.inlaco.crewmgrservice.feature.contract.presentation.dto.request;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.DynamicAttribute;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DynamicContractRequest extends AbstractContractRequest {

  public DynamicContractRequest() {
    super(ContractType.DYNAMIC_CONTRACT);
  }

  public DynamicContractRequest(ContractType type) {
    super(type);
  }

  private List<DynamicAttribute> customAttributes = new ArrayList<>();
}
