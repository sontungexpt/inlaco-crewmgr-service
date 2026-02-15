package com.inlaco.crewmgrservice.feature.contract.domain.model;

<<<<<<< Updated upstream
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
import com.inlaco.crewmgrservice.feature.contract.domain.objectvalue.DynamicAttribute;
=======
import com.inlaco.crewmgrservice.common.model.DynamicAttribute;
import com.inlaco.crewmgrservice.feature.contract.domain.enums.ContractType;
>>>>>>> Stashed changes
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DynamicContract extends AbstractContract {

  public DynamicContract() {
    super(ContractType.DYNAMIC_CONTRACT);
  }

  public DynamicContract(ContractType type) {
    super(type);
  }

  private List<DynamicAttribute> customAttributes = new ArrayList<>();
}
