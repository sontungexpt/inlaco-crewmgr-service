package com.inlaco.crewmgrservice.feature.shipschedule.application.port.in;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceQRCode;

public interface AttendanceQRCodeUseCase {
  AttendanceQRCode generateCheckInQR(String shipScheduleId, String employeeCardId);
  AttendanceQRCode generateCheckOutQR(String shipScheduleId, String employeeCardId);
  AttendanceQRCode verifyQR(String token, String deviceId, String location);
  boolean isValidQR(String token);
}
