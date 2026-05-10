package com.inlaco.crewmgrservice.feature.shipschedule.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.QrVerifyCommand;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.AttendanceQRCodeUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceQRCode;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.VerifyAttendanceRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.VerifyQRAttendanceRequest;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.config.openapi.OpenApiConfig;
import com.inlaco.crewmgrservice.infrastructure.web.annotation.CurrentUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
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

  @GetMapping("/{shipScheduleId}/{employeeCardId}")
  @Operation(
      summary = "Generate a QR code for check-in",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed("ADMIN")
  public AttendanceQRCode generateCheckInQRCode(
      @RequestParam CheckType checkType,
      @PathVariable String shipScheduleId,
      @PathVariable String employeeCardId,
      @RequestParam(defaultValue = "QR_CODE") AttendanceMethod method,
      @CurrentUser User authenticatedUser) {

    switch (method) {
      case QR_CODE:
        return qrCodeUseCase.generateQRCode(
            shipScheduleId, employeeCardId, checkType, authenticatedUser.getId());

      default:
        throw new IllegalArgumentException("Unsupported method: " + method);
    }
  }

  @PostMapping("/verify/{shipScheduleId}")
  @Operation(
      summary = "Verify a QR code for check-in or check-out",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  public AttendanceLog verify(
      @Valid @RequestBody VerifyAttendanceRequest request,
      @PathVariable String shipScheduleId,
      @CurrentUser User user) {

    switch (request.getType()) {
      case QR_CODE:
        VerifyQRAttendanceRequest qrRequest = (VerifyQRAttendanceRequest) request;
        QrVerifyCommand command =
            QrVerifyCommand.builder()
                .token(qrRequest.getToken())
                .checkType(qrRequest.getCheckType())
                .deviceId(qrRequest.getDeviceId())
                .location(qrRequest.getLocation())
                .build();

        return qrCodeUseCase.verifyQR(command, user.getId());
      default:
        throw new IllegalArgumentException("Unsupported method: " + request.getType());
    }
  }
}
