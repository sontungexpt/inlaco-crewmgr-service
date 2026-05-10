package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.QrVerifyCommand;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.AttendanceQRCodeUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.AttendanceLogRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.AttendanceQRCodeRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleCrewAssignmentRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.errors.AttendanceErrorCode;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.exception.AttendanceQRCodeException;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceQRCode;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import java.time.Duration;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceQRCodeService implements AttendanceQRCodeUseCase {

  private static final Duration DEFAULT_QR_DURATION = Duration.ofMinutes(5);

  private final AttendanceQRCodeRepository qrCodeRepository;
  private final AttendanceLogRepository attendanceLogRepository;
  private final ShipScheduleCrewAssignmentRepository assignmentRepository;

  @Override
  public AttendanceQRCode generateQRCode(
      String shipScheduleId, String employeeCardId, CheckType checkType, String userId) {

    AttendanceQRCode qrCode =
        AttendanceQRCode.generate(shipScheduleId, employeeCardId, checkType, DEFAULT_QR_DURATION);

    return qrCodeRepository.save(qrCode);
  }

  @Override
  @Transactional
  public AttendanceLog verifyQR(QrVerifyCommand command, String userId) {
    String token = command.getToken();

    AttendanceQRCode qrCode =
        qrCodeRepository
            .findByToken(token)
            .orElseThrow(
                () ->
                    new AttendanceQRCodeException(
                        AttendanceErrorCode.ATTENDANCE_QR_NOT_FOUND, "QR code not found"));

    qrCode.verify();

    CheckType expectedType = command.getCheckType();
    if (qrCode.getType() != expectedType) {

      throw new AttendanceQRCodeException(
          AttendanceErrorCode.ATTENDANCE_QR_INVALID_TYPE, "Invalid QR type");
    }

    ShipScheduleCrewAssignment assignment =
        assignmentRepository
            .findByAccountIdAndScheduleId(userId, qrCode.getShipScheduleId())
            .orElseThrow(
                () ->
                    new AttendanceQRCodeException(
                        AttendanceErrorCode.ATTENDANCE_QR_NOT_FOUND,
                        "Crew not assigned to this schedule"));

    String employeeCardId = assignment.getEmployeeCardId();

    if (!qrCode.getEmployeeCardId().equals(employeeCardId)) {
      throw new AttendanceQRCodeException(
          AttendanceErrorCode.ATTENDANCE_QR_INVALID_OWNER,
          "QR code does not belong to current user");
    }

    validateAttendanceState(employeeCardId, qrCode.getShipScheduleId(), expectedType);

    AttendanceLog log =
        AttendanceLog.builder()
            .crewAccountId(userId)
            .crewProfileId(assignment.getProfileId())
            .crewEmployeeCardId(employeeCardId)
            .crewName(assignment.getFullName())
            .crewRankOnBoard(assignment.getRankOnBoard())
            .shipScheduleId(qrCode.getShipScheduleId())
            .timestamp(Instant.now())
            .checkType(expectedType)
            .method(AttendanceMethod.QR_CODE)
            .location(command.getLocation())
            .build();

    AttendanceLog savedLog = attendanceLogRepository.save(log);

    qrCodeRepository.deleteByToken(token);

    return savedLog;
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
