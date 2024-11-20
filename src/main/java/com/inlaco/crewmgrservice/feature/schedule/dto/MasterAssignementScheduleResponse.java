package com.inlaco.crewmgrservice.feature.schedule.dto;

import com.inlaco.crewmgrservice.common.model.Address;
import com.inlaco.crewmgrservice.feature.schedule.model.ShipInfo;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.bson.types.ObjectId;

@AllArgsConstructor
@Getter
@Setter
public class MasterAssignementScheduleResponse {

  @Schema(description = "The id of ther MasterAssignementSchedule")
  private String id;

  @Schema(description = "The name of the partner company", requiredMode = RequiredMode.REQUIRED)
  private String partnerName;

  // NOTE: This is not implemented in the current version of the application
  // Because of the lack of information about the partner company
  private ObjectId partnerId;

  @Schema(description = "The information of the ship", requiredMode = RequiredMode.REQUIRED)
  private ShipInfo shipInfo;

  @Schema(description = "The start date of the work schedule")
  private Instant startDate;

  @Schema(
      description = "The estimated completion time of the work schedule",
      requiredMode = RequiredMode.REQUIRED)
  private Instant estimatedEndTime;

  @Schema(description = "The start location of the work schedule")
  private Address startLocation;

  @Schema(description = "The end location of the work schedule")
  private Address endLocation;
}
