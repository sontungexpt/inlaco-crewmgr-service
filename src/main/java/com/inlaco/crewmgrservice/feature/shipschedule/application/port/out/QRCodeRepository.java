package com.inlaco.crewmgrservice.feature.shipschedule.application.port.out;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.QRCode;
import java.util.Optional;

public interface QRCodeRepository {
  QRCode save(QRCode qrCode);
  Optional<QRCode> findByToken(String token);
  Optional<QRCode> findByTokenAndNotUsed(String token);
  void deleteById(String id);
}
