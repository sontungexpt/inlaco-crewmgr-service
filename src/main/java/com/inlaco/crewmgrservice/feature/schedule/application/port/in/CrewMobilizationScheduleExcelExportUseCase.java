package com.inlaco.crewmgrservice.feature.schedule.application.port.in;

public interface CrewMobilizationScheduleExcelExportUseCase {
  byte[] exportSchedule(String scheduleId);
}
