package com.inlaco.crewmgrservice.feature.contract.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.common.model.Versionable;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.Instant;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@SuperBuilder
@Getter
@Setter
@JsonIgnoreProperties(
    value = {"id", "version", "createdAt", "updatedAt", "prevVersion"},
    allowGetters = true)
@Document(collection = "contract_versions")
@NoArgsConstructor
/** The document to save all versions of contract */
public abstract class ContractVersion implements Versionable<String, Integer> {

  @Id
  @Schema(hidden = true)
  @JsonPatchIgnore
  private String id;

  @Schema(description = "The id of the first version", hidden = true)
  @JsonPatchIgnore
  private String firstVersionId;

  public boolean isFirstVersion() {
    return firstVersionId == null;
  }

  @Schema(description = "The id of the next version", hidden = true)
  @JsonPatchIgnore
  private String nextVersionId;

  @Schema(description = "The id of the previous version", hidden = true)
  @JsonPatchIgnore
  private String prevVersionId;

  @Schema(description = "The version number of the contract", hidden = true)
  @JsonPatchIgnore
  private int version = 1;

  @Schema(
      description =
          "The previous version number of the contract."
              + " If it is the first version, it will be null.",
      hidden = true)
  @JsonPatchIgnore
  private Integer prevVersion = null;

  @Schema(
      description =
          "The next version number of the contract."
              + " If it is the last version, it will be null.",
      hidden = true)
  @JsonPatchIgnore
  private Integer nextVersion = null;

  @JsonPatchIgnore
  @Schema(description = "The change log file of this version", hidden = true)
  private File changeLogFile;

  @Schema(
      description = "The short change log of this version",
      example = "Change the title",
      requiredMode = RequiredMode.REQUIRED)
  @JsonPatchIgnore
  private String changeLog;

  @CreatedDate
  @Schema(hidden = true)
  @JsonPatchIgnore
  private Instant createdAt;

  @Schema(hidden = true)
  @LastModifiedDate
  @JsonPatchIgnore
  private Instant updatedAt;

  @Schema(hidden = true)
  @CreatedBy
  @JsonPatchIgnore
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId createdBy;

  @Schema(hidden = true)
  @LastModifiedBy
  @JsonPatchIgnore
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId updatedBy;

  @Override
  @Schema(hidden = true)
  public String getId() {
    return id;
  }

  @Override
  @Schema(hidden = true)
  public Integer getVersion() {
    return version;
  }

  @Override
  @Schema(hidden = true)
  public String getChangeLog() {
    return changeLog;
  }

  @Override
  @Schema(hidden = true)
  public Integer getPrevVersion() {
    return prevVersion;
  }

  @Override
  @Schema(hidden = true)
  @JsonIgnore
  public Instant getCreatedAt() {
    return createdAt;
  }

  @Override
  @Schema(hidden = true)
  @JsonIgnore
  public Instant getUpdatedAt() {
    return updatedAt;
  }

  @Override
  @JsonIgnore
  public ObjectId getCreatedBy() {
    return createdBy;
  }

  @Override
  @JsonIgnore
  public ObjectId getUpdatedBy() {
    return updatedBy;
  }

  @Override
  public Integer getNextVersion() {
    return nextVersion;
  }

  @Override
  public String getFirstVersionId() {
    return firstVersionId;
  }

  @Override
  public String getPrevVersionId() {
    return prevVersionId;
  }

  @Override
  public String getNextVersionId() {
    return nextVersionId;
  }
}
