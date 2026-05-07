//package com.inlaco.crewmgrservice.feature.shipschedule.presentation.rest.controller;
//
//import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
//import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
//import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.CreateShipScheduleRequest;
//import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.UpdateCrewListRequest;
//import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.UpdateShipScheduleRequest;
//import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.ShipScheduleResponse;
//import com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper.ShipScheduleMapper;
//import io.swagger.v3.oas.annotations.Operation;
//import io.swagger.v3.oas.annotations.Parameter;
//import io.swagger.v3.oas.annotations.tags.Tag;
//import jakarta.validation.Valid;
//import java.time.Instant;
//import java.util.List;
//import lombok.RequiredArgsConstructor;
//import org.springframework.format.annotation.DateTimeFormat;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.*;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/v1/ship-schedules")
//@Tag(name = "Ship Schedule Management", description = "APIs for managing ship schedules and crew assignments")
//public class ShipScheduleController {
//
//  private final ShipScheduleUseCase shipScheduleUseCase;
//  private final ShipScheduleMapper mapper;
//
//  @PostMapping
//  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER')")
//  @Operation(summary = "Create a new ship schedule")
//  public ResponseEntity<ShipScheduleResponse> createSchedule(@Valid @RequestBody CreateShipScheduleRequest request) {
//    ShipSchedule schedule = mapper.toDomain(request);
//    ShipSchedule created = shipScheduleUseCase.createSchedule(schedule);
//    return ResponseEntity.ok(mapper.toResponse(created));
//  }
//
//  @PutMapping("/{id}")
//  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER')")
//  @Operation(summary = "Update an existing ship schedule")
//  public ResponseEntity<ShipScheduleResponse> updateSchedule(
//      @PathVariable String id,
//      @Valid @RequestBody UpdateShipScheduleRequest request) {
//    ShipSchedule schedule = mapper.toDomain(request);
//    ShipSchedule updated = shipScheduleUseCase.updateSchedule(id, schedule);
//    return ResponseEntity.ok(mapper.toResponse(updated));
//  }
//
//  @DeleteMapping("/{id}")
//  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER')")
//  @Operation(summary = "Delete a ship schedule")
//  public ResponseEntity<Void> deleteSchedule(@PathVariable String id) {
//    shipScheduleUseCase.deleteSchedule(id);
//    return ResponseEntity.noContent().build();
//  }
//
//  @GetMapping("/{id}")
//  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER') or hasRole('VIEWER')")
//  @Operation(summary = "Get ship schedule by ID")
//  public ResponseEntity<ShipScheduleResponse> getSchedule(@PathVariable String id) {
//    return shipScheduleUseCase.getScheduleById(id)
//        .map(schedule -> ResponseEntity.ok(mapper.toResponse(schedule)))
//        .orElse(ResponseEntity.notFound().build());
//  }
//
//  @GetMapping("/client/{clientId}")
//  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER') or hasRole('VIEWER')")
//  @Operation(summary = "Get ship schedules by client ID")
//  public ResponseEntity<List<ShipScheduleResponse>> getSchedulesByClient(@PathVariable String clientId) {
//    List<ShipSchedule> schedules = shipScheduleUseCase.getSchedulesByClientId(clientId);
//    return ResponseEntity.ok(mapper.toResponseList(schedules));
//  }
//
//  @GetMapping("/ship/{shipImo}")
//  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER') or hasRole('VIEWER')")
//  @Operation(summary = "Get ship schedules by ship IMO")
//  public ResponseEntity<List<ShipScheduleResponse>> getSchedulesByShip(@PathVariable String shipImo) {
//    List<ShipSchedule> schedules = shipScheduleUseCase.getSchedulesByShipImo(shipImo);
//    return ResponseEntity.ok(mapper.toResponseList(schedules));
//  }
//
//  @GetMapping("/date-range")
//  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER') or hasRole('VIEWER')")
//  @Operation(summary = "Get ship schedules by date range")
//  public ResponseEntity<List<ShipScheduleResponse>> getSchedulesByDateRange(
//      @Parameter(description = "Start date (ISO format)")
//      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant startTime,
//      @Parameter(description = "End date (ISO format)")
//      @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) Instant endTime) {
//    List<ShipSchedule> schedules = shipScheduleUseCase.getSchedulesByDateRange(startTime, endTime);
//    return ResponseEntity.ok(mapper.toResponseList(schedules));
//  }
//
//  @GetMapping
//  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER') or hasRole('VIEWER')")
//  @Operation(summary = "Get all ship schedules")
//  public ResponseEntity<List<ShipScheduleResponse>> getAllSchedules() {
//    List<ShipSchedule> schedules = shipScheduleUseCase.getAllSchedules();
//    return ResponseEntity.ok(mapper.toResponseList(schedules));
//  }
//
//  @PostMapping("/{scheduleId}/crew")
//  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER')")
//  @Operation(summary = "Add crew member to ship schedule")
//  public ResponseEntity<ShipScheduleResponse> addCrewMember(
//      @PathVariable String scheduleId,
//      @RequestParam String employeeCardId) {
//    ShipSchedule updated = shipScheduleUseCase.addCrewMember(scheduleId, employeeCardId);
//    return ResponseEntity.ok(mapper.toResponse(updated));
//  }
//
//  @DeleteMapping("/{scheduleId}/crew/{employeeCardId}")
//  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER')")
//  @Operation(summary = "Remove crew member from ship schedule")
//  public ResponseEntity<ShipScheduleResponse> removeCrewMember(
//      @PathVariable String scheduleId,
//      @PathVariable String employeeCardId) {
//    ShipSchedule updated = shipScheduleUseCase.removeCrewMember(scheduleId, employeeCardId);
//    return ResponseEntity.ok(mapper.toResponse(updated));
//  }
//
//  @PutMapping("/{scheduleId}/crew")
//  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER')")
//  @Operation(summary = "Update entire crew list for ship schedule")
//  public ResponseEntity<ShipScheduleResponse> updateCrewList(
//      @PathVariable String scheduleId,
//      @Valid @RequestBody UpdateCrewListRequest request) {
//    ShipSchedule updated = shipScheduleUseCase.updateCrewList(scheduleId, request.getEmployeeCardIds());
//    return ResponseEntity.ok(mapper.toResponse(updated));
//  }
//}
