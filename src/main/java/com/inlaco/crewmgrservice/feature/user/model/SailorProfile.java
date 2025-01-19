package com.inlaco.crewmgrservice.feature.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.common.model.File;
import com.inlaco.crewmgrservice.feature.user.enums.WorkStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@JsonIgnoreProperties(
    value = {"id", "cardId", "accountId", "contractId", "joinedAt", "updatedAt", "candidateId"},
    allowGetters = true)
@CompoundIndexes({
  @CompoundIndex(
      name = "profile_search_index",
      def = "{'fullName': 1, 'email': 1, 'phoneNumber': 1}")
})
@Document(collection = "sailors")
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
public class SailorProfile extends BasicProfile {

  public enum UploadableType {
    SOCIAL_INSURANCE,
    ACCIDENT_INSURANCE
  }

  @Schema(
      description = "The work status of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  @JsonPatchIgnore
  @JsonIgnore
  private WorkStatus workStatus;

  @Schema(description = "The candidate id of the sailor", hidden = true, type = "String")
  @JsonPatchIgnore
  @JsonSerialize(using = ToStringSerializer.class)
  protected ObjectId candidateId;

  @Schema(description = "The position of the sailor", requiredMode = RequiredMode.REQUIRED)
  private String professionalPosition;

  @Schema(
      description = "The date the sailor joined the company",
      requiredMode = RequiredMode.REQUIRED,
      type = "Date")
  @JsonIgnore
  @JsonPatchIgnore
  private Instant joinedCompanyAt;

  @Schema(
      description = "The card id (Year-STT)",
      example = "202200001",
      requiredMode = RequiredMode.REQUIRED,
      hidden = true,
      type = "string")
  @JsonPatchIgnore
  @Indexed(unique = true)
  protected String cardId;

  @Schema(
      description = "The expertise levels of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "List<String>")
  @NotNull
  protected List<String> expertiseLevels;

  @Schema(
      description = "The language skills of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "List<String>")
  @NotNull
  protected List<String> languageSkills;

  @Schema(
      description = "The experience of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "List<String>")
  protected List<String> experiences;

  @Schema(
      description = "The social insurance code of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  protected String socialInsuranceCode;

  @Schema(
      description = "The social insurance image of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "List<File>")
  protected List<File> socialInsuranceImages;

  @Schema(
      description = "The health insurance code of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "String")
  protected String accidentInsuranceCode;

  @Schema(
      description = "The health insurance image of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "List<File>")
  protected List<File> accidentInsuranceImages;

  @JsonIgnore
  @JsonPatchIgnore
  @LastModifiedDate
  @Schema(hidden = true)
  private Instant updatedAt;

  @JsonIgnore
  @CreatedDate
  @JsonPatchIgnore
  @Schema(hidden = true)
  private Instant createdAt;

  @JsonIgnore
  @JsonPatchIgnore
  @CreatedBy
  @Schema(hidden = true)
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId createdBy;

  @JsonIgnore
  @Schema(hidden = true)
  @LastModifiedBy
  @JsonPatchIgnore
  @JsonSerialize(using = ToStringSerializer.class)
  private ObjectId updatedBy;
}
