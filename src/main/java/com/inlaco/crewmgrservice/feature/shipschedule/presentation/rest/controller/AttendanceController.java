package com.inlaco.crewmgrservice.feature.shipschedule.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.AttendanceLogSearchCriteria;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.QrVerifyCommand;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.AttendanceLogUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.AttendanceQRCodeUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.VerifyAttendanceRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.VerifyQRAttendanceRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.AttendanceLogResponse;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.AttendanceQRCodeResponse;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper.AttendanceMapper;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.Filter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/attendance")
@Tag(
    name = "Attendance Management",
    description = "APIs for generating and verifying QR codes for attendance tracking")
public class AttendanceController {

  private final AttendanceQRCodeUseCase qrCodeUseCase;
  private final AttendanceMapper mapper;
  private final AttendanceLogUseCase logUseCase;

  @GetMapping("/{shipScheduleId}")
  @Operation(
      summary = "Generate a QR code for check-in",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  public AttendanceQRCodeResponse generateCheckInQRCode(
      @RequestParam CheckType checkType,
      @PathVariable String shipScheduleId,
      @RequestParam(defaultValue = "QR_CODE") AttendanceMethod method,
      @CurrentUser User authenticatedUser) {

    switch (method) {
      case QR_CODE:
        return mapper.toAttendanceQRCodeResponse(
            qrCodeUseCase.generateQRCode(shipScheduleId, checkType, authenticatedUser.getId()));

      default:
        throw new IllegalArgumentException("Unsupported method: " + method);
    }
  }

  @Operation(
      summary = "Get all logs for a ship schedule",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @GetMapping("/{shipScheduleId}/logs")
  public Page<AttendanceLogResponse> getLogs(
      @Filter AttendanceLogSearchCriteria criteria,
      @PathVariable String shipScheduleId,
      @PageableDefault(page = 0, size = 10) Pageable pageable) {
    if (criteria == null) {
      criteria = new AttendanceLogSearchCriteria();
    }
    criteria.setShipScheduleId(shipScheduleId);
    return logUseCase.getLogs(criteria, pageable).map(mapper::toAttendanceLogResponse);
  }

  @PostMapping("/verify")
  @Operation(
      summary = "Verify a QR code for check-in or check-out",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  public AttendanceLogResponse verify(
      @Valid @RequestBody VerifyAttendanceRequest request, @CurrentUser User user) {
    switch (request.getType()) {
      case QR_CODE:
        VerifyQRAttendanceRequest qrRequest = (VerifyQRAttendanceRequest) request;
        QrVerifyCommand command =
            QrVerifyCommand.builder()
                .token(qrRequest.getToken())
                .deviceId(qrRequest.getDeviceId())
                .location(qrRequest.getLocation())
                .build();

        return mapper.toAttendanceLogResponse(qrCodeUseCase.verifyQR(command, user.getId()));
      default:
        throw new IllegalArgumentException("Unsupported method: " + request.getType());
    }
  }
}
