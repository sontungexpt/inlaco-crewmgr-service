package com.inlaco.crewmgrservice.common.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DynamicAttribute {

  private String key;

  private Object value;
}
