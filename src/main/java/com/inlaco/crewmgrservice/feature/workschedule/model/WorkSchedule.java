package com.inlaco.crewmgrservice.feature.workschedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.common.model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document("work_schedules")
@JsonIgnoreProperties(
    value = {"id"},
    allowGetters = true)
public class WorkSchedule {

  @Schema(hidden = true)
  @Id
  private String id;

  @com.inlaco.crewmgrservice.validation.annotation.ObjectId
  @Schema(
      description = "The ID of the sailor that this work schedule is associated with",
      hidden = true)
  private ObjectId sailorId;

  @Schema(description = "The ID of the partner")
  private ObjectId partnerId;

  @Schema(description = "The start date of the work schedule")
  private Instant startDate;

  @Schema(description = "The end date of the work schedule")
  private Instant endDate;

  @Schema(description = "The start location of the work schedule")
  private Address startLocation;

  @Schema(description = "The end location of the work schedule")
  private Address endLocation;

  @CreatedDate
  @JsonIgnore
  @Schema(hidden = true)
  private Instant createdAt;

  @JsonIgnore
  @Schema(hidden = true)
  @LastModifiedDate
  private Instant updatedAt;
}
