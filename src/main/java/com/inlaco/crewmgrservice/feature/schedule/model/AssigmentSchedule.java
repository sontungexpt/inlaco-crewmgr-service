package com.inlaco.crewmgrservice.feature.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.common.model.ShipInfo;
import com.inlaco.crewmgrservice.validation.annotation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.Instant;
import java.util.Set;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

@Builder
@Getter
@Setter
@JsonIgnoreProperties(
    value = {"id", "createdAt", "updatedAt", "totalSailors"},
    allowGetters = true)
@Document(collection = "master_assignment_schedules")
public class AssigmentSchedule implements Serializable {

  @Schema(description = "The status of the schedule")
  public enum Status {
    @Schema(description = "The schedule is pending")
    PENDING,

    @Schema(description = "The schedule is in progress")
    IN_PROGRESS,

    @Schema(description = "The schedule is completed")
    COMPLETED
  }

  @Schema(description = "The status of the schedule", requiredMode = RequiredMode.REQUIRED)
  private Status status;

  @Id
  @Schema(hidden = true)
  @JsonPatchIgnore
  private String id;

  @Schema(description = "The name of the partner company", requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String partnerName;

  @Schema(description = "Phone number of the company.", example = "+1234567890", required = true)
  @NotBlank
  @PhoneNumber
  private String partnerPhone;

  @Schema(
      description = "Email address of the company.",
      example = "contact@shippingcompany.com",
      requiredMode = RequiredMode.REQUIRED)
  @Email
  @NotBlank
  private String partnerEmail;

  @Schema(
      description = "Address of the company.",
      example = "123 Maritime Ave, Port City",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String partnerAddress;

  @Schema(description = "Total number of crew members needed.", example = "10", required = true)
  @NotNull
  @Min(1)
  private Integer totalSailors;

  @Schema(
      description = "Departure point.",
      example = "Port of Los Angeles",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String departurePoint;

  @Schema(
      description = "Arrival point.",
      example = "Port of Tokyo",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String arrivalPoint;

  @Schema(
      description = "UN/LOCODE for the departure point.",
      example = "USLAX",
      requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String departureUNLOCODE;

  @Schema(description = "UN/LOCODE for the arrival point.", example = "JPTYO", required = true)
  @NotBlank
  private String arrivalUNLOCODE;

  @NotNull
  @Schema(description = "The information of the ship", requiredMode = RequiredMode.REQUIRED)
  private ShipInfo shipInfo;

  @Schema(
      description = "The start date of the work schedule",
      example = "2025-01-14T10:00:00Z",
      requiredMode = RequiredMode.REQUIRED)
  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Future
  private Instant startDate;

  @Schema(
      description = "Estimated arrival time.",
      example = "2025-01-20T18:00:00Z",
      requiredMode = RequiredMode.REQUIRED)
  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Future
  private Instant estimatedEndDate;

  @Data
  public static class CrewMember {

    @Indexed
    @Schema(description = "The ID of the crew member's card", requiredMode = RequiredMode.REQUIRED)
    private String cardId;

    @Schema(description = "The name of the crew member", requiredMode = RequiredMode.REQUIRED)
    private String professionalPosition;

    @Override
    public int hashCode() {
      return cardId.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
      if (this == obj) return true;
      else if (obj instanceof CrewMember that) {
        return this.cardId.equals(that.cardId);
      }
      return false;
    }
  }

  private Set<CrewMember> crewMembers;

  private boolean done;

  @CreatedDate
  @JsonIgnore
  @Schema(hidden = true)
  @JsonPatchIgnore
  private Instant createdAt;

  @JsonIgnore
  @Schema(hidden = true)
  @LastModifiedDate
  @JsonPatchIgnore
  private Instant updatedAt;

  public Instant getUpdatedDate() {
    return updatedAt;
  }

  @CreatedBy
  @JsonIgnore
  @Schema(hidden = true)
  @JsonPatchIgnore
  private ObjectId createdBy;

  @JsonIgnore
  @Schema(hidden = true)
  @LastModifiedBy
  @JsonPatchIgnore
  private ObjectId updatedBy;
}
