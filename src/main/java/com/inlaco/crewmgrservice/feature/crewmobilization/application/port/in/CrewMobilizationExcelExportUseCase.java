package com.inlaco.crewmgrservice.feature.crewmobilization.application.port.in;

public interface CrewMobilizationExcelExportUseCase {
  byte[] exportMobilization(String scheduleId);
}
