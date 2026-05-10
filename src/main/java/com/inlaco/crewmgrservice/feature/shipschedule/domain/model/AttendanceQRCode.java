package com.inlaco.crewmgrservice.feature.shipschedule.domain.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.errors.AttendanceErrorCode;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.exception.AttendanceQRCodeException;
import java.time.Duration;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceQRCode {

  private String token;

  private String shipScheduleId;

  private String employeeCardId;

  private CheckType type;

  private Instant expiresAt;

  private Instant createdAt;

  public static AttendanceQRCode generate(
      String shipScheduleId, String employeeCardId, CheckType type, Duration duration) {

    String prefix = type.name().toLowerCase();

    Instant now = Instant.now();

    return AttendanceQRCode.builder()
        .token(prefix + "_" + NanoIdUtils.randomNanoId())
        .shipScheduleId(shipScheduleId)
        .employeeCardId(employeeCardId)
        .type(type)
        .createdAt(now)
        .expiresAt(now.plus(duration))
        .build();
  }

  public boolean isExpired() {
    return expiresAt != null && Instant.now().isAfter(expiresAt);
  }

  public void verify() {

    if (isExpired()) {
      throw new AttendanceQRCodeException(
          AttendanceErrorCode.ATTENDANCE_QR_EXPIRED, "QR code expired");
    }
  }
}
