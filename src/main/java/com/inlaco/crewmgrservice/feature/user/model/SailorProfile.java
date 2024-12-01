package com.inlaco.crewmgrservice.feature.user.model;

import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Pattern;
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
import org.springframework.format.annotation.DateTimeFormat;

@SuperBuilder
@Getter
@Setter
@Document(collection = "sailors")
@NoArgsConstructor
@AllArgsConstructor
public class SailorProfile extends BasicProfile {

  private SailorPosition position;

  @CreatedDate private Instant joinedAt;

  @LastModifiedDate private Instant updatedAt;

  @Schema(description = "The card id", requiredMode = RequiredMode.REQUIRED, type = "string")
  @Pattern(regexp = "^[0-9]*${12}")
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
      type = "String")
  protected String experience;

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
}
