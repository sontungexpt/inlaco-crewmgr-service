package com.inlaco.crewmgrservice.feature.user.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.common.payload.TimeFrame;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotNull;
import java.time.Instant;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.util.Pair;
import org.springframework.format.annotation.DateTimeFormat;

@SuperBuilder
@Getter
@Setter
@Document(collection = "sailors")
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(
    value = {"id", "cardId"},
    allowGetters = true)
public class SailorProfile extends BasicProfile implements TimeFrame {

  protected String contractId;

  @Schema(description = "The position of the sailor", requiredMode = RequiredMode.REQUIRED)
  @NotNull
  private SailorPosition position;

  @Schema(
      description = "The date the sailor joined the company",
      requiredMode = RequiredMode.REQUIRED,
      type = "Date")
  @CreatedDate
  @JsonIgnore
  private Instant joinedAt;

  @Schema(
      description = "The date the sailor profile was last updated",
      requiredMode = RequiredMode.REQUIRED,
      type = "Date")
  @LastModifiedDate
  @JsonIgnore
  private Instant updatedAt;

  @Schema(
      description = "The card id (Year-STT)",
      example = "2022-00001",
      requiredMode = RequiredMode.REQUIRED,
      type = "string")
  protected String cardId;

  @Schema(
      description = "The expertise levels of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "List<String>")
  protected List<String> expertiseLevels;

  @Schema(
      description = "The language skills of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "List<String>")
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

  @Override
  public List<Pair<Instant, Instant>> getTimeFrames() {
    return List.of(Pair.of(socialInsuranceStartDate, socialInsuranceEndDate));
  }
}
