package com.inlaco.crewmgrservice.feature.contract.presentation.dto.response;

import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.DynamicAttribute;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DynamicContractResponse extends AbstractContractResponse {

  public DynamicContractResponse() {
    super(ContractType.DYNAMIC_CONTRACT);
  }

  public DynamicContractResponse(ContractType type) {
    super(type);
  }

  private List<DynamicAttribute> customAttributes = new ArrayList<>();
}
