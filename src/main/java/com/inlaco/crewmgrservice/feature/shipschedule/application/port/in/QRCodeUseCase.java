package com.inlaco.crewmgrservice.feature.shipschedule.application.port.in;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.QRCode;

public interface QRCodeUseCase {
  QRCode generateCheckInQR(String shipScheduleId, String employeeCardId);
  QRCode generateCheckOutQR(String shipScheduleId, String employeeCardId);
  QRCode verifyQR(String token, String deviceId, String location);
  boolean isValidQR(String token);
}
