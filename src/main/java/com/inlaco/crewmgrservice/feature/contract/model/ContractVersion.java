package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.common.model.Versionable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.Instant;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@SuperBuilder
@Getter
@Setter
@JsonIgnoreProperties(
    value = {"id", "version", "createdAt", "updatedAt", "prevVersion"},
    allowGetters = true)
@Document(collection = "contract_versions")
/** The document to save all versions of contract */
public abstract class ContractVersion implements Versionable<String, Integer> {

  @Id
  @Schema(hidden = true)
  @JsonPatchIgnore
  private String id;

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

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

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

  @Override
  public Instant getCreatedAt() {
    return createdAt;
  }

  @Override
  public Instant getUpdatedAt() {
    return updatedAt;
  }
}
