package com.inlaco.crewmgrservice.feature.contract.domain.objectvalue;

import lombok.Data;

@Data
public class DynamicAttribute {

  private String key;

  private Object value;
}
