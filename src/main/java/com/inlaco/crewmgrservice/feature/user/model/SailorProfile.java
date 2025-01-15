package com.inlaco.crewmgrservice.feature.user.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.common.payload.TimeFrame;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
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
import org.springframework.format.annotation.DateTimeFormat;

@SuperBuilder
@Getter
@Setter
@Document(collection = "sailors")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(
    value = {"id", "cardId", "contractId", "joinedAt", "updatedAt", "candidateId"},
    allowGetters = true)
@CompoundIndexes({
  @CompoundIndex(
      name = "profile_search_index",
      def = "{'fullName': 1, 'email': 1, 'phoneNumber': 1}")
})
public class SailorProfile extends BasicProfile implements TimeFrame {

  @Default
  @JsonIgnore
  @JsonPatchIgnore
  @Schema(hidden = true, description = "The contract ids of the sailor", type = "List<String>")
  protected Set<ObjectId> contractIds = new HashSet<>();

  @JsonGetter("contractIds")
  public List<String> getContractIdsStr() {
    return contractIds.stream().map(ObjectId::toHexString).toList();
  }

  @Schema(description = "The candidate id of the sailor", hidden = true, type = "String")
  @JsonPatchIgnore
  protected ObjectId candidateId;

  @NotNull
  @Schema(description = "The position of the sailor", requiredMode = RequiredMode.REQUIRED)
  private String professionalPosition;

  @Schema(
      description = "The date the sailor joined the company",
      requiredMode = RequiredMode.REQUIRED,
      type = "Date")
  @JsonIgnore
  @JsonPatchIgnore
  private Instant joinedAt;

  @Schema(
      description = "The card id (Year-STT)",
      example = "2022-00001",
      requiredMode = RequiredMode.REQUIRED,
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
      description = "The social insurance start date of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "Date")
  @Future
  @DateTimeFormat
  protected Instant socialInsuranceStartDate;

  @Schema(
      description = "The social insurance end date of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "Date")
  @Future
  @DateTimeFormat
  protected Instant socialInsuranceEndDate;

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

  @Schema(hidden = true)
  @JsonIgnore
  @JsonPatchIgnore
  @CreatedBy
  private ObjectId createdBy;

  @LastModifiedBy
  @JsonIgnore
  @JsonPatchIgnore
  @Schema(hidden = true)
  private ObjectId updatedBy;

  @Override
  @JsonIgnore
  @Schema(hidden = true)
  public List<Pair> getTimeFrames() {
    return List.of(Pair.of(socialInsuranceStartDate, socialInsuranceEndDate));
  }
}
