package com.inlaco.crewmgrservice.feature.shipschedule.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleDetail;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.ShipScheduleSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.CreateShipScheduleRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.ShipScheduleResponse;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper.ShipScheduleMapper;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
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
      @Valid @RequestBody CreateShipScheduleRequest request, User user) {

    ShipSchedule schedule = mapper.toShipSchedule(request);
    List<ShipScheduleCrewAssignment> assignments =
        request.getCrews().stream().map(mapper::toShipScheduleCrewAssignment).toList();
    ShipSchedule created = shipScheduleUseCase.createSchedule(schedule, assignments, user);

    return ResponseEntity.ok(mapper.toShipScheduleResponse(created));
  }

  @Operation(
      summary = "Get a ship schedule detail",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/{id}")
  public ShipScheduleDetail getScheduleDetail(@PathVariable String id) {
    return shipScheduleUseCase.getScheduleDetail(id);
  }

  @Operation(
      summary = "Get all ship schedules",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("")
  public Page<ShipScheduleResponse> getAllSchedules(
      @Filter ShipScheduleSearchCriteria criteria,
      @PageableDefault(page = 0, size = 10) Pageable pageable) {
    return shipScheduleUseCase.getSchedules(criteria, pageable).map(mapper::toShipScheduleResponse);
  }

  @GetMapping("/me")
  @Operation(
      summary = "Get all ship schedules for authenticated client",
      description =
          "Retrieve all current ship schedules belonging to authenticated client using API key"
              + " authentication")
  @RolesAllowed({"USER", "SAILOR"})
  public Page<ShipScheduleResponse> getMySchedules(
      @Filter ShipScheduleSearchCriteria criteria,
      @PageableDefault(page = 0, size = 10) Pageable pageable,
      @CurrentUser User user,
      Authentication authentication) {
    if (criteria == null) {
      criteria = new ShipScheduleSearchCriteria();
    }

    boolean isSailor =
        authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SAILOR"));
    if (isSailor) {
      criteria.setCrewAccountId(user.getId());
    } else {
      criteria.setVesselOwnerId(user.getId());
    }
    return shipScheduleUseCase.getSchedules(criteria, pageable).map(mapper::toShipScheduleResponse);
  }
}
