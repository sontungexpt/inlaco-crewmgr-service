package com.inlaco.crewmgrservice.feature.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.Instant;
import java.util.List;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class SailorProfileDTO extends BasicProfileDTO {

  @Schema(description = "The contract ifacet d of the sailor", requiredMode = RequiredMode.REQUIRED)
  protected String contractId;

  @Schema(description = "The position of the sailor", requiredMode = RequiredMode.REQUIRED)
  private String professionalPosition;

  @Schema(
      description = "The date the sailor joined the company",
      requiredMode = RequiredMode.REQUIRED,
      type = "Date")
  private Instant joinedAt;

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
  protected Instant socialInsuranceStartDate;

  @Schema(
      description = "The social insurance end date of the sailor",
      requiredMode = RequiredMode.REQUIRED,
      type = "Date")
  protected Instant socialInsuranceEndDate;

  @Schema(hidden = true)
  private Instant updatedAt;

  @Schema(hidden = true)
  private Instant createdAt;
}
