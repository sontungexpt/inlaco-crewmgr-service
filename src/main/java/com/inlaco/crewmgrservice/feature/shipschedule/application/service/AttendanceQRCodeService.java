package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.AttendanceQrClaims;
import com.inlaco.crewmgrservice.feature.shipschedule.application.model.QrVerifyCommand;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.AttendanceQRCodeUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.AttendanceLogRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleCrewAssignmentRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.error.AttendanceErrorCode;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.exception.AttendanceQRCodeException;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceQRCode;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceQRCodeService implements AttendanceQRCodeUseCase {

  private final AttendanceLogRepository attendanceLogRepository;
  private final ShipScheduleCrewAssignmentRepository assignmentRepository;
  private final AttendanceQrTokenService qrTokenService;

  @Override
  public AttendanceQRCode generateQRCode(
      String shipScheduleId, CheckType checkType, String userId) {

    AttendanceQrClaims claims =
        AttendanceQrClaims.builder()
            .shipScheduleId(shipScheduleId)
            .checkType(checkType)
            .method(AttendanceMethod.QR_CODE)
            .generatedBy(userId)
            .generatedAt(Instant.now())
            .build();

    String token = qrTokenService.generate(claims);

    return AttendanceQRCode.builder().token(token).build();
  }

  @Override
  @Transactional
  public AttendanceLog verifyQR(QrVerifyCommand command, String userId) {

    AttendanceQrClaims claims = qrTokenService.parse(command.getToken());

    CheckType expectedType = claims.getCheckType();
    String shipScheduleId = claims.getShipScheduleId();

    // Check if user is assigned to this schedule
    ShipScheduleCrewAssignment assignment =
        assignmentRepository
            .findByAccountIdAndScheduleId(userId, shipScheduleId)
            .orElseThrow(
                () ->
                    new AttendanceQRCodeException(
                        AttendanceErrorCode.ATTENDANCE_QR_NOT_FOUND,
                        "Crew not assigned to this schedule"));

    String employeeCardId = assignment.getEmployeeCardId();

    validateDeviceUsage(shipScheduleId, command.getDeviceId(), userId);
    validateAttendanceState(employeeCardId, shipScheduleId, expectedType);

    AttendanceLog log =
        AttendanceLog.builder()
            .crewAccountId(userId)
            .crewProfileId(assignment.getProfileId())
            .crewEmployeeCardId(employeeCardId)
            .crewName(assignment.getFullName())
            .crewRankOnBoard(assignment.getRankOnBoard())
            .shipScheduleId(shipScheduleId)
            .timestamp(Instant.now())
            .checkType(expectedType)
            .method(AttendanceMethod.QR_CODE)
            .location(command.getLocation())
            .deviceId(command.getDeviceId())
            .build();

    return attendanceLogRepository.save(log);
  }

  private void validateDeviceUsage(String shipScheduleId, String deviceId, String currentUserId) {
    if (deviceId == null || deviceId.trim().isEmpty()) {
      return;
    }

    AttendanceLog existingLog =
        attendanceLogRepository
            .findLastByDeviceIdInShipSchedule(deviceId, shipScheduleId)
            .orElse(null);

    if (existingLog != null && !existingLog.getCrewAccountId().equals(currentUserId)) {
      throw new AttendanceQRCodeException(
          AttendanceErrorCode.ATTENDANCE_DEVICE_ALREADY_USED,
          "Device is already being used by another user");
    }
  }

  private void validateAttendanceState(
      String employeeCardId, String shipScheduleId, CheckType nextType) {

    AttendanceLog latest =
        attendanceLogRepository
            .findLatestByEmployeeCardIdAndShipScheduleId(employeeCardId, shipScheduleId)
            .orElse(null);

    if (latest == null) {
      if (nextType == CheckType.OUT) {
        throw new AttendanceQRCodeException(
            AttendanceErrorCode.ATTENDANCE_INVALID_SEQUENCE, "Cannot check out before check in");
      }
      return;
    }

    if (latest.getCheckType() == nextType) {

      if (nextType == CheckType.IN) {
        throw new AttendanceQRCodeException(
            AttendanceErrorCode.ATTENDANCE_ALREADY_CHECKED_IN, "Crew already checked in");
      }

      throw new AttendanceQRCodeException(
          AttendanceErrorCode.ATTENDANCE_ALREADY_CHECKED_OUT, "Crew already checked out");
    }
  }
}
