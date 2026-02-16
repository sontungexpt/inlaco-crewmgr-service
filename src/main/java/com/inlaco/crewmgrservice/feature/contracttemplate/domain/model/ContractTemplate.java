package com.inlaco.crewmgrservice.feature.contracttemplate.domain.model;

import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import lombok.Data;

@Data
public class ContractTemplate {

  private String id;

  private String name;

  private String description;

  private Asset metadata;

  private String type;
}
