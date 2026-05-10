package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.AttendanceQRCodeUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.AttendanceQRCodeRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceQRCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AttendanceQRCodeService implements AttendanceQRCodeUseCase {

  private final AttendanceQRCodeRepository qrCodeRepository;

  @Override
  public AttendanceQRCode generateCheckInQR(String shipScheduleId, String employeeCardId) {
    AttendanceQRCode qrCode = AttendanceQRCode.generateForCheckIn(shipScheduleId, employeeCardId);
    return qrCodeRepository.save(qrCode);
  }

  @Override
  public AttendanceQRCode generateCheckOutQR(String shipScheduleId, String employeeCardId) {
    AttendanceQRCode qrCode = AttendanceQRCode.generateForCheckOut(shipScheduleId, employeeCardId);
    return qrCodeRepository.save(qrCode);
  }

  @Override
  public AttendanceQRCode verifyQR(String token, String deviceId, String location) {
    AttendanceQRCode qrCode =
        qrCodeRepository
            .findByTokenAndNotUsed(token)
            .filter(AttendanceQRCode::isValid)
            .orElse(null);

    if (qrCode != null) {
      qrCode.markAsUsed(deviceId, location);
      return qrCodeRepository.save(qrCode);
    }

    return null;
  }

  @Override
  public boolean isValidQR(String token) {
    return qrCodeRepository
        .findByTokenAndNotUsed(token)
        .map(AttendanceQRCode::isValid)
        .orElse(false);
  }
}
