package com.inlaco.crewmgrservice.feature.contracttemplate.presentation.rest.dto.response;

import com.inlaco.crewmgrservice.shared.objectvalue.AssetResponse;
import lombok.Data;

@Data
public class ContractTemplateResponse {

  private String id;

  private String name;

  private String description;

  private AssetResponse metadata;

  private String type;
}
