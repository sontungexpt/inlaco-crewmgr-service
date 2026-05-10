package com.inlaco.crewmgrservice.feature.shipschedule.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.crew.presentation.mapper.CrewProfileMapper;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.CreateShipScheduleRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.ShipScheduleResponse;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper.ShipScheduleMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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
  private final CrewProfileMapper crewProfileMapper;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER')")
  @Operation(summary = "Create a new ship schedule")
  public ResponseEntity<ShipScheduleResponse> createSchedule(
      @Valid @RequestBody CreateShipScheduleRequest request) {
    ShipSchedule schedule = mapper.toShipSchedule(request);
    ShipSchedule created = shipScheduleUseCase.createSchedule(schedule);
    return ResponseEntity.ok(mapper.toShipScheduleResponse(created));
  }
}
