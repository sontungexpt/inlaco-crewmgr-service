package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.common.model.Attachment;
import com.inlaco.crewmgrservice.common.model.Versionable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("contracts")
@JsonIgnoreProperties(
    value = {"id", "version"},
    allowGetters = true)
public class Contract implements Versionable<String, Integer> {

  @Id
  @Schema(hidden = true)
  private String id;

  @NotBlank
  @Schema(
      description = "The title of the contract",
      example = "The title of the contract",
      requiredMode = RequiredMode.REQUIRED)
  private String title;

  @Schema(
      description = "The description of the contract",
      example = "{\"name\":\"Hop dong\", \"url\": \"https://....\"}")
  private List<Attachment> attachments;

  @Schema(
      description = "The terms of the contract",
      example = "The terms of the contract",
      requiredMode = RequiredMode.REQUIRED)
  @NotEmpty
  private List<@NotBlank String> terms;

  @Schema(description = "The version number of the contract", hidden = true)
  private int version = 1;

  @Schema(
      description =
          "The previous version number of the contract."
              + " If it is the first version, it will be null.",
      hidden = true)
  private Integer prevVersion = null;

  @Schema(
      description = "The short change log of this version",
      example = "Change the title",
      requiredMode = RequiredMode.REQUIRED)
  private String changeLog;

  @Override
  public String getId() {
    return id;
  }

  @Override
  public Integer getVersion() {
    return version;
  }

  @Override
  public String getChangeLog() {
    return changeLog;
  }

  @Override
  public Integer getPrevVersion() {
    return prevVersion;
  }
}
