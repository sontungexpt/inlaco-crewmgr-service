package com.inlaco.crewmgrservice.feature.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.inlaco.crewmgrservice.common.model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;

@Builder
@Document(collection = "master_assignment_schedules")
@Getter
@Setter
public class MasterAssignmentSchedule implements Serializable {

  @Id
  @Schema(hidden = true)
  private String id;

  @Schema(description = "The ID of the partner", requiredMode = RequiredMode.REQUIRED)
  @com.inlaco.crewmgrservice.validation.annotation.ObjectId
  private ObjectId partnerId;

  @Schema(description = "The name of the partner", requiredMode = RequiredMode.REQUIRED)
  private String partnerName;

  @NotNull
  @Schema(description = "The information of the ship", requiredMode = RequiredMode.REQUIRED)
  private ShipInfo shipInfo;

  @Schema(description = "The start date of the work schedule")
  private Instant startDate;

  @Schema(description = "The end date of the work schedule")
  private Instant endDate;

  @Schema(description = "The start location of the work schedule")
  private Address startLocation;

  @Schema(description = "The end location of the work schedule")
  private Address endLocation;

  @Schema(
      description = "The estimated completion time of the work schedule",
      requiredMode = RequiredMode.REQUIRED)
  @NotNull
  private Instant estimatedCompletionTime;

  @CreatedDate
  @JsonIgnore
  @Schema(hidden = true)
  private Instant createdAt;

  @JsonIgnore
  @Schema(hidden = true)
  @LastModifiedDate
  private Instant updatedAt;
}
