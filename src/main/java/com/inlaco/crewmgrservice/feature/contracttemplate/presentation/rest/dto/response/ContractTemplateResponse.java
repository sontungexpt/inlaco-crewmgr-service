package com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.dto.response;

import com.inlaco.crewmgrservice.common.model.Asset;
import lombok.Data;

@Data
public class ContractTemplateResponse {

  private String id;

  private String name;

  private String description;

  private Asset metadata;

  private String type;
}
