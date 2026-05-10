package com.inlaco.crewmgrservice.feature.shipschedule.application.port.out;

import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceQRCode;
import java.util.Optional;

public interface AttendanceQRCodeRepository {
  AttendanceQRCode save(AttendanceQRCode qrCode);

  Optional<AttendanceQRCode> findByToken(String token);

  Optional<AttendanceQRCode> findByTokenAndNotUsed(String token);

  void deleteById(String id);
}
