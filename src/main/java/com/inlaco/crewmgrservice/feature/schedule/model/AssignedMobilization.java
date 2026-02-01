package com.inlaco.crewmgrservice.feature.schedule.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.annotation.JsonPatchIgnore;
import com.inlaco.crewmgrservice.common.model.ShipInfo;
import com.inlaco.crewmgrservice.common.payload.TimeFrame;
import com.inlaco.crewmgrservice.validation.annotation.PhoneNumber;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.io.Serializable;
import java.time.Instant;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
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
    value = {"id", "status", "createdAt", "updatedAt"},
    allowGetters = true)
@Document(collection = "master_assignment_schedules")
@NoArgsConstructor
@AllArgsConstructor
public class AssignedMobilization implements Serializable, TimeFrame {

  @Schema(description = "The status of the schedule")
  public enum Status {
    @Schema(description = "The schedule is pending")
    PENDING,

    @Schema(description = "The schedule is in progress")
    IN_PROGRESS,

    @Schema(description = "The schedule is completed")
    COMPLETED
  }

  @Id
  @Schema(hidden = true)
  @JsonPatchIgnore
  private String id;

  @Schema(description = "The name of the partner company", requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String partnerName;

  @Schema(
      description = "Phone number of the company.",
      example = "+1234567890",
      requiredMode = RequiredMode.REQUIRED)
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

  @NotNull
  @Schema(description = "The information of the ship", requiredMode = RequiredMode.REQUIRED)
  private ShipInfo shipInfo;

  @Schema(description = "The start date of the work schedule", requiredMode = RequiredMode.REQUIRED)
  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @Future
  private Instant startDate;

  @Schema(
      description = "The end date of the work schedule",
      example = "2025-01-20T18:00:00Z",
      requiredMode = RequiredMode.REQUIRED)
  @NotNull
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant endDate;

  @Schema(description = "The status of the schedule", hidden = true)
  @JsonPatchIgnore
  private Status status;

  public int getTotalCrews() {
    return crewMembers == null ? 0 : crewMembers.size();
  }

  @Override
  @JsonIgnore
  public List<Pair> getTimeFrames() {
    return List.of(Pair.of(startDate, endDate));
  }

  @Schema(description = "The crew members assigned to the schedule")
  @Size(min = 1)
  private Set<@Valid CrewAssignment> crewMembers;

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

  @Schema(hidden = true)
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

  @Data
  @EqualsAndHashCode(of = "cardId")
  public static class CrewAssignment {

    @NotBlank @Indexed private String cardId;

    @NotBlank private String rankOnBoard;

    @NotNull private Instant startDate;

    @NotNull private Instant endDate;

    private String remark;
  }
}
