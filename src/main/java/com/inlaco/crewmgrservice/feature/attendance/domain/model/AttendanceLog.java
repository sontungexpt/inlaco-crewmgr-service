package com.inlaco.crewmgrservice.feature.attendance.domain.model;

import com.inlaco.crewmgrservice.feature.attendance.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.attendance.domain.enums.CheckType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class AttendanceLog {

  @Id private String id;

  private String scheduleId;

  private String crewId;

  private CheckType checkType;

  private AttendanceMethod method;

  private String location;

  private String deviceId;

  /**
   * Indicates whether this attendance log is verified as valid after system validation.
   *
   * <p>true -> The check-in/check-out is legitimate: - QR token is valid (signature, expiration,
   * correct schedule) - Crew belongs to the schedule - No duplicate or invalid sequence (e.g.
   * check-out before check-in) - (Optional) location/device constraints satisfied
   *
   * <p>false -> The attempt was recorded but considered invalid: - Invalid or expired QR - Crew not
   * in schedule - Duplicate scan or suspicious behavior - Any rule violation during verification
   *
   * <p>Note: - Logs should still be stored even if not verified for auditing and fraud detection.
   */
  private boolean verified;

  private Instant createdAt;
}
