package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceQRCode;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "attendance_qr_codes")
public class AttendanceQRCodeEntity {
  
  @Id
  private String id;
  private String token;
  private String shipScheduleId;
  private String employeeCardId;
  private CheckType type;
  private Instant expiresAt;
  private boolean used;
  private Instant usedAt;
  private String deviceId;
  private String location;
  private Instant createdAt;

  public static AttendanceQRCodeEntity fromDomain(AttendanceQRCode qrCode) {
    return AttendanceQRCodeEntity.builder()
        .id(qrCode.getId())
        .token(qrCode.getToken())
        .shipScheduleId(qrCode.getShipScheduleId())
        .employeeCardId(qrCode.getEmployeeCardId())
        .type(qrCode.getType())
        .expiresAt(qrCode.getExpiresAt())
        .used(qrCode.isUsed())
        .usedAt(qrCode.getUsedAt())
        .deviceId(qrCode.getDeviceId())
        .location(qrCode.getLocation())
        .createdAt(qrCode.getCreatedAt())
        .build();
  }

  public AttendanceQRCode toDomain() {
    return AttendanceQRCode.builder()
        .id(id)
        .token(token)
        .shipScheduleId(shipScheduleId)
        .employeeCardId(employeeCardId)
        .type(type)
        .expiresAt(expiresAt)
        .used(used)
        .usedAt(usedAt)
        .deviceId(deviceId)
        .location(location)
        .createdAt(createdAt)
        .build();
  }
}
