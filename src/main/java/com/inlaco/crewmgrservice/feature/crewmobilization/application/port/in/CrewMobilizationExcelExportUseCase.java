package com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in;

public interface CrewMobilizationExcelExportUseCase {
  byte[] exportSchedule(String scheduleId);
}
