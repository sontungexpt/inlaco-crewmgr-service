package com.inlaco.crewmgrservice.feature.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Null;
import java.time.Instant;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("crew_work_schedules")
@JsonIgnoreProperties(
    value = {
      "id",
      "sailorId",
      "sailorPositionId",
      "masterAssignmentScheduleId",
      "createdAt",
      "updatedAt"
    },
    allowGetters = true)
public class SailorWorkSchedule {

  @Id
  @Schema(hidden = true)
  private String id;

  @Schema(
      description = "The ID of the sailor that this work schedule is associated with",
      type = "String",
      example = "5f7f1b3b7f4b7b001f3b3b7f",
      hidden = true)
  @Null
  private ObjectId sailorId;

  @Schema(
      description = "The ID of the partner",
      type = "String",
      example = "5f7f1b3b7f4b7b001f3b3b7f",
      requiredMode = RequiredMode.REQUIRED)
  @Null
  private ObjectId sailorPositionId;

  @Schema(description = "The ID of the master assignment schedule", hidden = true)
  @Null
  private ObjectId masterAssignmentScheduleId;

  @CreatedDate
  @JsonIgnore
  @Schema(hidden = true)
  private Instant createdAt;

  @JsonIgnore
  @Schema(hidden = true)
  @LastModifiedDate
  private Instant updatedAt;
}
