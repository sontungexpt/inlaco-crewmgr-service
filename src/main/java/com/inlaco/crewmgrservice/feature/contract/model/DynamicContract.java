package com.inlaco.crewmgrservice.feature.contract.model;

import com.inlaco.crewmgrservice.common.model.DynamicAttribute;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.ArrayList;
import java.util.List;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
@Setter
public class DynamicContract extends AbstractContract {

  @Schema(
      description = "The list of custom attributes of the contract",
      example = "[{\"name\":\"custom1\", \"value\": \"value1\"}]")
  @Default
  private List<DynamicAttribute> customAttributes = new ArrayList<>();
}
