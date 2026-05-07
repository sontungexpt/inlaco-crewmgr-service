//package com.inlaco.crewmgrservice.feature.shipschedule.presentation.rest.controller;
//
//import com.inlaco.crewmgrservice.feature.apikey.presentation.rest.controller.BaseExternalApiController;
//import com.inlaco.crewmgrservice.feature.apikey.presentation.annotation.RequireApiKey;
//import com.inlaco.crewmgrservice.feature.crew.application.port.in.CrewUseCase;
//import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
//import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
//import com.inlaco.crewmgrservice.feature.crew.domain.model.CrewProfile;
//import com.inlaco.crewmgrservice.feature.crew.presentation.dto.response.CrewProfileResponse;
//import com.inlaco.crewmgrservice.feature.crew.presentation.mapper.CrewProfileMapper;
//import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.ShipScheduleCrewResponse;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.core.Authentication;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Optional;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/external/ship-schedules")
//@Tag(name = "Ship Schedule External API", description = "External APIs for accessing ship schedule and crew information")
//@RequireApiKey(value = "Ship Schedule Management", feature = "crew-management")
//public class ExternalApiController extends BaseExternalApiController {
//
//  private final ShipScheduleUseCase shipScheduleUseCase;
//  private final CrewUseCase crewUseCase;
//  private final CrewProfileMapper crewProfileMapper;
//
//  @Override
//  protected boolean hasAccessToResource(Authentication authentication, String resourceId) {
//    // For ship schedules, allow access if:
//    // 1. Client is authenticated
//    // 2. Resource exists (will be checked in controller methods)
//    // 3. Optional: Add client-specific access rules here
//    // For now, allow all authenticated clients to access ship schedule data
//    return authentication != null && authentication.isAuthenticated();
//  }
//
//  @GetMapping("/{scheduleId}/crew")
//  @Operation(summary = "Get crew information for a specific ship schedule",
//             description = "Requires API key authentication via X-API-Key-ID and X-API-Key-Secret headers")
//  public ResponseEntity<ShipScheduleCrewResponse> getScheduleCrewInfo(
//      @PathVariable String scheduleId,
//      Authentication authentication) {
//
//    // Use base class helper to check access
//    if (!hasAccessToResource(authentication, scheduleId)) {
//      return ResponseEntity.status(403).build();
//    }
//
//    Optional<ShipSchedule> scheduleOpt = shipScheduleUseCase.getScheduleById(scheduleId);
//    if (scheduleOpt.isEmpty()) {
//      return ResponseEntity.notFound().build();
//    }
//
//    ShipSchedule schedule = scheduleOpt.get();
//
//    // Get crew member details
//    List<CrewProfileResponse> crewMembers = crewUseCase
//        .getProfilesByEmployeeCardIds(schedule.getEmployeeCardIds())
//        .stream()
//        .map(crewProfileMapper::toResponse)
//        .toList();
//
//    ShipScheduleCrewResponse response = new ShipScheduleCrewResponse();
//    response.setId(schedule.getId());
//    response.setClientId(schedule.getClientId());
//    response.setShipImo(schedule.getShipImo());
//    response.setShipName(schedule.getShipName());
//    response.setRoute(schedule.getRoute());
//    response.setDepartureTime(schedule.getDepartureTime());
//    response.setArrivalTime(schedule.getArrivalTime());
//    response.setDeparturePort(schedule.getDeparturePort());
//    response.setArrivalPort(schedule.getArrivalPort());
//    response.setStatus(schedule.getStatus());
//    response.setCrewMembers(crewMembers);
//    response.setCreatedAt(schedule.getCreatedAt());
//    response.setUpdatedAt(schedule.getUpdatedAt());
//
//    return ResponseEntity.ok(response);
//  }
//
//  @GetMapping("/{scheduleId}/crew/{employeeCardId}")
//  @Operation(summary = "Get specific crew member information from a ship schedule")
//  public ResponseEntity<CrewProfileResponse> getCrewMemberInfo(
//      @PathVariable String scheduleId,
//      @PathVariable String employeeCardId,
//      Authentication authentication) {
//
//    // Use base class helper to check access
//    if (!hasAccessToResource(authentication, scheduleId)) {
//      return ResponseEntity.status(403).build();
//    }
//
//    Optional<ShipSchedule> scheduleOpt = shipScheduleUseCase.getScheduleById(scheduleId);
//    if (scheduleOpt.isEmpty()) {
//      return ResponseEntity.notFound().build();
//    }
//
//    ShipSchedule schedule = scheduleOpt.get();
//    if (!schedule.getEmployeeCardIds().contains(employeeCardId)) {
//      return ResponseEntity.notFound().build();
//    }
//
//    return crewUseCase.getProfilesByEmployeeCardIds(List.of(employeeCardId))
//        .stream()
//        .findFirst()
//        .map(crewProfileMapper::toResponse)
//        .map(ResponseEntity::ok)
//        .orElse(ResponseEntity.notFound().build());
//  }
//
//  @GetMapping
//  @Operation(summary = "Get all ship schedules (basic info without crew details)")
//  public ResponseEntity<List<ShipScheduleCrewResponse>> getAllSchedulesBasic(Authentication authentication) {
//    List<ShipSchedule> schedules = shipScheduleUseCase.getAllSchedules();
//
//    List<ShipScheduleCrewResponse> responses = schedules.stream()
//        .map(schedule -> {
//          ShipScheduleCrewResponse response = new ShipScheduleCrewResponse();
//          response.setId(schedule.getId());
//          response.setClientId(schedule.getClientId());
//          response.setShipImo(schedule.getShipImo());
//          response.setShipName(schedule.getShipName());
//          response.setRoute(schedule.getRoute());
//          response.setDepartureTime(schedule.getDepartureTime());
//          response.setArrivalTime(schedule.getArrivalTime());
//          response.setDeparturePort(schedule.getDeparturePort());
//          response.setArrivalPort(schedule.getArrivalPort());
//          response.setStatus(schedule.getStatus());
//          response.setCreatedAt(schedule.getCreatedAt());
//          response.setUpdatedAt(schedule.getUpdatedAt());
//          // Don't include crew members in basic view
//          return response;
//        })
//        .toList();
//
//    return ResponseEntity.ok(responses);
//  }
//}
