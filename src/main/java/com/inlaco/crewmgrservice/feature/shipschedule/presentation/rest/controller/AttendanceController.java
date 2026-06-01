package com.inlaco.crewmgrservice.feature.shipschedule.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.QrVerifyCommand;
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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.annotation.security.RolesAllowed;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
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

  @GetMapping("/{shipScheduleId}/logs")
  @Operation(
      summary = "Get attendance history by ship schedule",
      security = {@SecurityRequirement(name = OpenApiConfig.BEARER_AUTH_NAME)})
  @RolesAllowed({"ADMIN", "USER", "SAILOR"})
  public List<AttendanceLogResponse> getAttendanceHistory(
      @PathVariable String shipScheduleId,
      @CurrentUser User user,
      Authentication authentication) {
    boolean isAdmin =
        authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ADMIN"));
    boolean isSailor =
        authentication.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("SAILOR"));

    return qrCodeUseCase.getAttendanceHistory(shipScheduleId, user.getId(), isAdmin, isSailor)
        .stream()
        .map(mapper::toAttendanceLogResponse)
        .toList();
  }
}
