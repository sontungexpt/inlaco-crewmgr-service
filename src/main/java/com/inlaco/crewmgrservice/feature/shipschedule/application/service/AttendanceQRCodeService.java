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

  private static final double MAX_DISTANCE_METERS = 100;

  private final AttendanceLogRepository attendanceLogRepository;
  private final ShipScheduleCrewAssignmentRepository assignmentRepository;
  private final AttendanceQrTokenService qrTokenService;

  @Override
  public AttendanceQRCode generateQRCode(
      String shipScheduleId, CheckType checkType, String location, String userId) {

    AttendanceQrClaims claims =
        AttendanceQrClaims.builder()
            .shipScheduleId(shipScheduleId)
            .checkType(checkType)
            .method(AttendanceMethod.QR_CODE)
            .location(location)
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

    String location = claims.getLocation();

    if (location != null
        && !location.isBlank()
        && command.getLocation() != null
        && !command.getLocation().isBlank()) {

      validateLocation(claims.getLocation(), command.getLocation());
    }

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

  private void validateLocation(String expectedLocation, String actualLocation) {

    try {

      String[] expected = expectedLocation.split(",");
      String[] actual = actualLocation.split(",");

      double expectedLat = Double.parseDouble(expected[0]);
      double expectedLng = Double.parseDouble(expected[1]);

      double actualLat = Double.parseDouble(actual[0]);
      double actualLng = Double.parseDouble(actual[1]);

      double accuracy = actual.length >= 3 ? Double.parseDouble(actual[2]) : 0;

      double distance = calculateDistanceMeters(expectedLat, expectedLng, actualLat, actualLng);

      double allowedDistance = Math.max(MAX_DISTANCE_METERS, accuracy * 2);

      if (distance > allowedDistance) {
        throw new AttendanceQRCodeException(
            AttendanceErrorCode.ATTENDANCE_INVALID_LOCATION,
            "You are too far from attendance location");
      }

    } catch (Exception e) {

      throw new AttendanceQRCodeException(
          AttendanceErrorCode.ATTENDANCE_INVALID_LOCATION, "Invalid location data");
    }
  }

  private double calculateDistanceMeters(double lat1, double lon1, double lat2, double lon2) {

    final int EARTH_RADIUS = 6371000;

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);

    double a =
        Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
            + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2)
                * Math.sin(lonDistance / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS * c;
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
