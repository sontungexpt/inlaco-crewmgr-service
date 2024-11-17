package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("contract_templates")
@JsonIgnoreProperties(
    value = {"id", "templateUrl"},
    allowGetters = true)
public class ContractTemplate {

  @Schema(hidden = true)
  @Id
  private String id;

  @Schema(
      description = "The name of the template",
      example = "Template 1",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String name;

  @Schema(
      description = "The description of the template",
      example = "Desciprtion of template 1",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String description;

  @NotBlank private String templateUrl;

  @Schema(
      description = "The type of the contract",
      enumAsRef = true,
      requiredMode = RequiredMode.REQUIRED)
  private ContractType contractType;
}
