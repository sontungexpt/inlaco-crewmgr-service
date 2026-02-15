package com.inlaco.crewmgrservice.feature.contracttemplate.domain.model;

import com.inlaco.crewmgrservice.common.model.Asset;
import lombok.Data;

@Data
public class ContractTemplate {

  private String id;

  private String name;

  private String description;

  private Asset metadata;

  private String type;
}
