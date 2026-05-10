package com.inlaco.crewmgrservice.feature.shipschedule.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.AttendanceQRCodeUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.service.CrewAssignmentService;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper.AttendanceQRCodeMapper;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendance")
@Tag(
    name = "Attendance Management",
    description = "APIs for generating and verifying QR codes for attendance tracking")
public class AttendanceController {

  private final AttendanceQRCodeUseCase qrCodeUseCase;
  private final ShipScheduleUseCase shipScheduleUseCase;
  private final AttendanceQRCodeMapper qrCodeMapper;
  private final CrewAssignmentService crewAssignmentService;
}
