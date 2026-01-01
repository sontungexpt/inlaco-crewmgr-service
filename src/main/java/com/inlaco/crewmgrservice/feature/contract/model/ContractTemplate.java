package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.common.model.File;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Builder
@Document("contract_templates")
@JsonIgnoreProperties(
    value = {"id", "metadata"},
    allowGetters = true)
@NoArgsConstructor
@AllArgsConstructor
public class ContractTemplate implements Serializable {

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
  private String description;

  @Schema(
      description = "The metadata of the template",
      example = "Metadata of template 1",
      requiredMode = RequiredMode.REQUIRED)
  private File metadata;

  @NotNull
  @Schema(
      description = "The type of the contract",
      enumAsRef = true,
      requiredMode = RequiredMode.REQUIRED)
  private ContractType type;
}
