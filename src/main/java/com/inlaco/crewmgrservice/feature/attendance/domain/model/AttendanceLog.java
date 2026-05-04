package com.inlaco.crewmgrservice.feature.attendance.domain.model;

import com.inlaco.crewmgrservice.feature.attendance.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.attendance.domain.enums.CheckType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "attendance_logs")
@AllArgsConstructor
@Getter
@Setter
public class AttendanceLog {

  @Id private String id;

  private String scheduleId;
  private String personId;
  private String companyId;
  private CheckType checkType;
  private AttendanceMethod method;
  private Instant timestamp;
  private String location;
  private String deviceId;
  private boolean verified;

  public static AttendanceLog createCheckIn(
      String scheduleId, String personId, String companyId, String location, String deviceId) {
    return new AttendanceLog(
        null,
        scheduleId,
        personId,
        companyId,
        CheckType.IN,
        AttendanceMethod.QR,
        Instant.now(),
        location,
        deviceId,
        true);
  }
}
