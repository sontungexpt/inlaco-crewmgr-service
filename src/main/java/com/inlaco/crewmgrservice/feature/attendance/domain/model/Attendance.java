package com.inlaco.crewmgrservice.feature.attendance.domain.model;

import com.inlaco.crewmgrservice.feature.attendance.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.attendance.domain.enums.AttendanceStatus;
import java.time.Instant;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class Attendance {

  private String id;
  private String userId;
  private String scheduleId;

  private Instant checkInAt;
  private Instant checkOutAt;

  private AttendanceStatus status;

  private AttendanceMethod checkInMethod; // QR, GPS
  private AttendanceMethod checkOutMethod;

  private String location; // optional (GPS)

  private Instant createdAt;
}
