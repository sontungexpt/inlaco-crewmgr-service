package com.inlaco.crewmgrservice.feature.shipschedule.application.model;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.AttendanceMethod;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AttendanceQrClaims {

  private String shipScheduleId;

  private CheckType checkType;

  private AttendanceMethod method;

  private String generatedBy;

  @Default private Instant generatedAt = Instant.now();
}
