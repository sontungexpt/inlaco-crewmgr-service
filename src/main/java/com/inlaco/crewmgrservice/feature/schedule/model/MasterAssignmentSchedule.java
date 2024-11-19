package com.inlaco.crewmgrservice.feature.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.common.model.Address;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Document(collection = "master_assignment_schedules")
@Getter
@Setter
@JsonIgnoreProperties(
    value = {"id", "createdAt", "updatedAt"},
    allowGetters = true)
public class MasterAssignmentSchedule implements Serializable {

  @Id
  @Schema(hidden = true)
  private String id;

  @Schema(description = "The name of the partner company", requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String partnerName;

  @NotNull
  @Schema(description = "The information of the ship", requiredMode = RequiredMode.REQUIRED)
  private ShipInfo shipInfo;

  @Schema(description = "The start date of the work schedule")
  @DateTimeFormat
  @Future
  private Instant startDate;

  @Schema(
      description = "The estimated completion time of the work schedule",
      requiredMode = RequiredMode.REQUIRED)
  @DateTimeFormat
  @Future
  private Instant estimatedEndTime;

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
