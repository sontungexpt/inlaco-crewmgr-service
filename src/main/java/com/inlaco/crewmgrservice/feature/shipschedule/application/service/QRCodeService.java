package com.inlaco.crewmgrservice.feature.shipschedule.application.service;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.in.QRCodeUseCase;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.QRCodeRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.QRCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class QRCodeService implements QRCodeUseCase {
  
  private final QRCodeRepository qrCodeRepository;
  
  @Override
  public QRCode generateCheckInQR(String shipScheduleId, String employeeCardId) {
    QRCode qrCode = QRCode.generateForCheckIn(shipScheduleId, employeeCardId);
    return qrCodeRepository.save(qrCode);
  }
  
  @Override
  public QRCode generateCheckOutQR(String shipScheduleId, String employeeCardId) {
    QRCode qrCode = QRCode.generateForCheckOut(shipScheduleId, employeeCardId);
    return qrCodeRepository.save(qrCode);
  }
  
  @Override
  public QRCode verifyQR(String token, String deviceId, String location) {
    QRCode qrCode = qrCodeRepository.findByTokenAndNotUsed(token)
        .filter(QRCode::isValid)
        .orElse(null);
    
    if (qrCode != null) {
      qrCode.markAsUsed(deviceId, location);
      return qrCodeRepository.save(qrCode);
    }
    
    return null;
  }
  
  @Override
  public boolean isValidQR(String token) {
    return qrCodeRepository.findByTokenAndNotUsed(token)
        .map(QRCode::isValid)
        .orElse(false);
  }
}
