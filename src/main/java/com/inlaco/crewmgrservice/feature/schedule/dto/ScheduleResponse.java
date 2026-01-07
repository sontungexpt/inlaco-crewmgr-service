package com.inlaco.crewmgrservice.feature.schedule.dto;

import com.inlaco.crewmgrservice.common.model.ShipInfo;
import com.inlaco.crewmgrservice.feature.schedule.model.AssigmentSchedule;
import com.inlaco.crewmgrservice.feature.schedule.model.AssigmentSchedule.Status;
import com.inlaco.crewmgrservice.feature.user.model.SailorProfile;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.List;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.format.annotation.DateTimeFormat;

@Getter
@Builder
@Setter
public class ScheduleResponse {

  @Schema(description = "The status of the schedule", requiredMode = RequiredMode.REQUIRED)
  private Status status;

  @Id
  @Schema(hidden = true)
  private String id;

  @Schema(description = "The name of the partner company", requiredMode = RequiredMode.REQUIRED)
  @NotBlank
  private String partnerName;

  @Schema(
      description = "Phone number of the company.",
      example = "+1234567890",
      requiredMode = RequiredMode.REQUIRED)
  private String partnerPhone;

  @Schema(
      description = "Email address of the company.",
      example = "contact@shippingcompany.com",
      requiredMode = RequiredMode.REQUIRED)
  private String partnerEmail;

  @Schema(
      description = "Address of the company.",
      example = "123 Maritime Ave, Port City",
      requiredMode = RequiredMode.REQUIRED)
  private String partnerAddress;

  @Schema(
      description = "Total number of crew members needed.",
      example = "10",
      requiredMode = RequiredMode.REQUIRED)
  private Integer totalSailors;

  @Schema(description = "The information of the ship", requiredMode = RequiredMode.REQUIRED)
  private ShipInfo shipInfo;

  @Schema(
      description = "The start date of the work schedule",
      example = "2025-01-14T10:00:00Z",
      requiredMode = RequiredMode.REQUIRED)
  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private Instant startDate;

  @Schema(
      description = "Estimated arrival time.",
      example = "2025-01-20T18:00:00Z",
      requiredMode = RequiredMode.REQUIRED)
  private Instant estimatedEndDate;

  private List<SailorProfile> crewMembers;

  private Instant createdAt;

  private Instant updatedAt;

  private ObjectId createdBy;

  private ObjectId updatedBy;

  public static ScheduleResponse from(AssigmentSchedule schedule) {
    return ScheduleResponse.builder()
        .id(schedule.getId())
        .status(schedule.getStatus())
        .partnerName(schedule.getPartnerName())
        .partnerPhone(schedule.getPartnerPhone())
        .partnerEmail(schedule.getPartnerEmail())
        .partnerAddress(schedule.getPartnerAddress())
        .totalSailors(schedule.getTotalSailors())
        .shipInfo(schedule.getShipInfo())
        .startDate(schedule.getStartDate())
        .estimatedEndDate(schedule.getEndDate())
        .createdAt(schedule.getCreatedAt())
        .updatedAt(schedule.getUpdatedAt())
        .createdBy(schedule.getCreatedBy())
        .updatedBy(schedule.getUpdatedBy())
        .build();
  }
}
