package com.inlaco.crewmgrservice.feature.contracttemplate.domain.model;

import com.inlaco.crewmgrservice.common.model.File;
import lombok.Data;

@Data
public class ContractTemplate {

  private String id;

  private String name;

  private String description;

  private File metadata;

  private String type;
}
