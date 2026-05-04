package com.inlaco.crewmgrservice.feature.attendance.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.attendance.application.dto.CheckInCommand;
import com.inlaco.crewmgrservice.feature.attendance.application.port.in.CheckInUseCase;
import com.inlaco.crewmgrservice.feature.attendance.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.attendance.presentation.dto.request.CheckInRequest;
import com.inlaco.crewmgrservice.feature.attendance.presentation.dto.response.AttendanceResponse;
import com.inlaco.crewmgrservice.feature.attendance.presentation.mapper.AttendanceMapper;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendance")
public class AttendanceController {

  private final CheckInUseCase checkInUseCase;
  private final AttendanceMapper mapper;

  @PostMapping("/check-in")
  @Operation(summary = "Check-in a user")
  public ResponseEntity<AttendanceResponse> checkIn(@Valid @RequestBody CheckInRequest request) {
    CheckInCommand command =
        new CheckInCommand(request.getQrToken(), request.getDeviceId(), request.getLocation());
    AttendanceLog log = checkInUseCase.checkIn(command);
    AttendanceResponse response = mapper.toAttendanceResponse(log);
    return ResponseEntity.ok(response);
  }
}
