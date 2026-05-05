package com.inlaco.crewmgrservice.feature.shipschedule.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.QRCodeUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.ShipScheduleUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.QRCode;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.request.VerifyQRRequest;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.QRCodeResponse;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.dto.response.VerifyQRResponse;
import com.inlaco.crewmgrservice.feature.shipschedule.presentation.mapper.QRCodeMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/qr-codes")
@Tag(name = "QR Code Management", description = "APIs for generating and verifying QR codes for check-in/check-out")
public class QRCodeController {
  
  private final QRCodeUseCase qrCodeUseCase;
  private final ShipScheduleUseCase shipScheduleUseCase;
  private final QRCodeMapper qrCodeMapper;
  
  @PostMapping("/check-in/{scheduleId}/{employeeCardId}")
  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER')")
  @Operation(summary = "Generate QR code for check-in")
  public ResponseEntity<QRCodeResponse> generateCheckInQR(
      @PathVariable String scheduleId,
      @PathVariable String employeeCardId) {
    
    // Verify that the schedule exists and the employee is assigned to it
    if (shipScheduleUseCase.getScheduleById(scheduleId).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    
    QRCode qrCode = qrCodeUseCase.generateCheckInQR(scheduleId, employeeCardId);
    QRCodeResponse response = qrCodeMapper.toResponse(qrCode);
    
    return ResponseEntity.ok(response);
  }
  
  @PostMapping("/check-out/{scheduleId}/{employeeCardId}")
  @PreAuthorize("hasRole('ADMIN') or hasRole('SCHEDULE_MANAGER')")
  @Operation(summary = "Generate QR code for check-out")
  public ResponseEntity<QRCodeResponse> generateCheckOutQR(
      @PathVariable String scheduleId,
      @PathVariable String employeeCardId) {
    
    // Verify that the schedule exists and the employee is assigned to it
    if (shipScheduleUseCase.getScheduleById(scheduleId).isEmpty()) {
      return ResponseEntity.notFound().build();
    }
    
    QRCode qrCode = qrCodeUseCase.generateCheckOutQR(scheduleId, employeeCardId);
    QRCodeResponse response = qrCodeMapper.toResponse(qrCode);
    
    return ResponseEntity.ok(response);
  }
  
  @PostMapping("/verify")
  @Operation(summary = "Verify QR code for check-in/check-out")
  public ResponseEntity<VerifyQRResponse> verifyQR(@Valid @RequestBody VerifyQRRequest request) {
    QRCode verifiedQR = qrCodeUseCase.verifyQR(request.getToken(), request.getDeviceId(), request.getLocation());
    
    VerifyQRResponse response = new VerifyQRResponse();
    if (verifiedQR != null) {
      response.setValid(true);
      response.setMessage("QR code verified successfully");
      response.setShipScheduleId(verifiedQR.getShipScheduleId());
      response.setEmployeeCardId(verifiedQR.getEmployeeCardId());
      response.setType(verifiedQR.getType());
      response.setUsedAt(verifiedQR.getUsedAt());
      response.setDeviceId(verifiedQR.getDeviceId());
      response.setLocation(verifiedQR.getLocation());
    } else {
      response.setValid(false);
      response.setMessage("Invalid or expired QR code");
    }
    
    return ResponseEntity.ok(response);
  }
  
  @GetMapping("/validate/{token}")
  @Operation(summary = "Check if QR code is valid (without marking it as used)")
  public ResponseEntity<VerifyQRResponse> validateQR(@PathVariable String token) {
    boolean isValid = qrCodeUseCase.isValidQR(token);
    
    VerifyQRResponse response = new VerifyQRResponse();
    response.setValid(isValid);
    response.setMessage(isValid ? "QR code is valid" : "QR code is invalid or expired");
    
    return ResponseEntity.ok(response);
  }
}
