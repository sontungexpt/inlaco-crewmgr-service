package com.inlaco.crewmgrservice.feature.shipschedule.domain.enums;

import lombok.experimental.FieldNameConstants;

@FieldNameConstants
public enum AttendanceMethod {
  @FieldNameConstants.Include
  QR_CODE,

  @FieldNameConstants.Include
  FACE,
}
