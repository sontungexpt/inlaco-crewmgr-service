package com.inlaco.crewmgrservice.feature.shipschedule.domain.model;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.QRType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class QRCode {
  private String id;
  private String token;
  private String shipScheduleId;
  private String employeeCardId;
  private QRType type;
  private Instant expiresAt;
  private boolean used;
  private Instant usedAt;
  private String deviceId;
  private String location;
  private Instant createdAt;
  
  public static QRCode generateForCheckIn(String shipScheduleId, String employeeCardId) {
    String token = "ci_" + System.currentTimeMillis() + "_" + employeeCardId;
    
    return QRCode.builder()
        .token(token)
        .shipScheduleId(shipScheduleId)
        .employeeCardId(employeeCardId)
        .type(QRType.CHECK_IN)
        .expiresAt(Instant.now().plusSeconds(24 * 60 * 60)) // 24 hours
        .used(false)
        .createdAt(Instant.now())
        .build();
  }
  
  public static QRCode generateForCheckOut(String shipScheduleId, String employeeCardId) {
    String token = "co_" + System.currentTimeMillis() + "_" + employeeCardId;
    
    return QRCode.builder()
        .token(token)
        .shipScheduleId(shipScheduleId)
        .employeeCardId(employeeCardId)
        .type(QRType.CHECK_OUT)
        .expiresAt(Instant.now().plusSeconds(24 * 60 * 60)) // 24 hours
        .used(false)
        .createdAt(Instant.now())
        .build();
  }
  
  public boolean isValid() {
    return !used && (expiresAt == null || expiresAt.isAfter(Instant.now()));
  }
  
  public void markAsUsed(String deviceId, String location) {
    this.used = true;
    this.usedAt = Instant.now();
    this.deviceId = deviceId;
    this.location = location;
  }
}
