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
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class AttendanceQRCodeService implements AttendanceQRCodeUseCase {

  private static final double MAX_DISTANCE_METERS = 20;
  private static final double MAX_EXPECTED_ACCURACY = 100;

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

    log.debug("QR claims: {}", claims.toString()); // Debu

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

    if (deviceId == null || deviceId.isBlank()) {
      return;
    }

    AttendanceLog existing =
        attendanceLogRepository
            .findLastByDeviceIdInShipSchedule(deviceId, shipScheduleId)
            .orElse(null);

    if (existing != null && !existing.getCrewAccountId().equals(currentUserId)) {

      log.warn(
          "Device already used. deviceId={}, currentUser={}, existingUser={}",
          deviceId,
          currentUserId,
          existing.getCrewAccountId());

      throw new AttendanceQRCodeException(
          AttendanceErrorCode.ATTENDANCE_DEVICE_ALREADY_USED,
          "Device is already being used by another user");
    }
  }

  private void validateLocation(String expectedLocation, String actualLocation) {

    try {

      String[] expected = expectedLocation.split(",");
      String[] actual = actualLocation.split(",");

      double distance =
          calculateDistanceMeters(
              Double.parseDouble(expected[0]),
              Double.parseDouble(expected[1]),
              Double.parseDouble(actual[0]),
              Double.parseDouble(actual[1]));

      double expectedAccuracy = expected.length >= 3 ? Double.parseDouble(expected[2]) : 0;
      double actualAccuracy = actual.length >= 3 ? Double.parseDouble(actual[2]) : 0;

      double allowedDistance = Math.max(MAX_DISTANCE_METERS, actualAccuracy * 2);

      log.debug(
          "Attendance location validation. expected={}, actual={}, distance={}, allowed={},"
              + " actualAccuracy={}, expectedAccuracy={}",
          expectedLocation,
          actualLocation,
          Math.round(distance),
          Math.round(allowedDistance),
          actualAccuracy,
          expectedAccuracy);

      if (actualAccuracy > MAX_EXPECTED_ACCURACY) {
        log.warn(
            "Skip location validation because QR location accuracy is poor. accuracy={}m",
            expectedAccuracy);

        return;
      }

      if (distance > allowedDistance) {

        log.warn(
            "Attendance location validation failed - distance={}m > allowed={}m",
            Math.round(distance),
            Math.round(allowedDistance));

        throw new AttendanceQRCodeException(
            AttendanceErrorCode.ATTENDANCE_INVALID_LOCATION,
            "You are too far from attendance location");
      }

    } catch (AttendanceQRCodeException e) {
      throw e;
    } catch (Exception e) {

      log.warn("Invalid location format. expected={}, actual={}", expectedLocation, actualLocation);

      throw new AttendanceQRCodeException(
          AttendanceErrorCode.ATTENDANCE_QR_INVALID, "Invalid location data");
    }
  }

  private double calculateDistanceMeters(double lat1, double lon1, double lat2, double lon2) {

    final int EARTH_RADIUS = 6371000;

    double latDistance = Math.toRadians(lat2 - lat1);
    double lonDistance = Math.toRadians(lon2 - lon1);

    // Use Haversine formula
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

        log.warn(
            "Attendance sequence invalid. employeeCardId={} tried OUT without IN", employeeCardId);

        throw new AttendanceQRCodeException(
            AttendanceErrorCode.ATTENDANCE_INVALID_SEQUENCE, "Cannot check out before check in");
      }

      return;
    }

    if (latest.getCheckType() != nextType) {
      return;
    }

    log.warn("Duplicate attendance detected. employeeCardId={}, type={}", employeeCardId, nextType);

    throw new AttendanceQRCodeException(
        nextType == CheckType.IN
            ? AttendanceErrorCode.ATTENDANCE_ALREADY_CHECKED_IN
            : AttendanceErrorCode.ATTENDANCE_ALREADY_CHECKED_OUT,
        nextType == CheckType.IN ? "Crew already checked in" : "Crew already checked out");
  }
}
