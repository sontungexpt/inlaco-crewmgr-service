package com.inlaco.crewmgrservice.feature.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
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
    value = {"id", "sailorId", "masterAssignmentScheduleId", "createdAt", "updatedAt"},
    allowGetters = true)
public class CrewWorkSchedule {

  @Id
  @Schema(hidden = true)
  private String id;

  @com.inlaco.crewmgrservice.validation.annotation.ObjectId
  @Schema(
      description = "The ID of the sailor that this work schedule is associated with",
      hidden = true)
  private ObjectId sailorId;

  @com.inlaco.crewmgrservice.validation.annotation.ObjectId
  @Schema(description = "The ID of the master assignment schedule", hidden = true)
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
