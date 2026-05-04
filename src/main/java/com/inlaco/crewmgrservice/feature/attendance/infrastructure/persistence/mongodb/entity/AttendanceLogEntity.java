package com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.attendance.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.attendance.domain.enums.CheckType;
import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "attendance_logs")
@Data
public class AttendanceLogEntity {
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
}
