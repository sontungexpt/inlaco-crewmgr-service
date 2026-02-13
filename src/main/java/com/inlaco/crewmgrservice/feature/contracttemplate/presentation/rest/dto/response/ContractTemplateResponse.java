package com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.dto.response;

import com.inlaco.crewmgrservice.common.model.File;
import lombok.Data;

@Data
public class ContractTemplateResponse {

  private String id;

  private String name;

  private String description;

  private File metadata;

  private String type;
}
