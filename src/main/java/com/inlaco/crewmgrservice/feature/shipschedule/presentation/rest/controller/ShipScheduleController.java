package com.inlaco.crewmgrservice.feature.shipschedule.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.CreateShipScheduleRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.ShipScheduleResponse;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper.ShipScheduleMapper;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/ship-schedules")
@Tag(
    name = "Ship Schedule Management",
    description = "APIs for managing ship schedules and crew assignments")
public class ShipScheduleController {

  private final ShipScheduleUseCase shipScheduleUseCase;
  private final ShipScheduleMapper mapper;

  @Operation(
      summary = "Create a new ship schedule",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @PostMapping("")
  public ResponseEntity<ShipScheduleResponse> createSchedule(
      @Valid @RequestBody CreateShipScheduleRequest request) {

    ShipSchedule schedule = mapper.toShipSchedule(request);
    List<ShipScheduleCrewAssignment> assignments =
        request.getCrews().stream().map(mapper::toShipScheduleCrewAssignment).toList();
    ShipSchedule created = shipScheduleUseCase.createSchedule(schedule, assignments);

    return ResponseEntity.ok(mapper.toShipScheduleResponse(created));
  }
}
