package com.inlaco.crewmgrservice.feature.schedule.domain.enums;

public enum CrewMobilizationScheduleStatus {
  DRAFT, // mới tạo, có thể chỉnh sửa
  SCHEDULED, // đã xác nhận, chờ đến ngày bắt đầu
  IN_PROGRESS, // đang diễn ra
  COMPLETED, // đã kết thúc
  CANCELLED // bị hủy
}
