package com.inlaco.crewmgrservice.feature.shipschedule.application.port.in;

import com.inlaco.crewmgrservice.feature.shipschedule.application.model.QrVerifyCommand;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceQRCode;
import java.util.List;

public interface AttendanceQRCodeUseCase {

  AttendanceQRCode generateQRCode(String shipScheduleId, CheckType checkType, String userId);

  AttendanceLog verifyQR(QrVerifyCommand command, String userId);

  List<AttendanceLog> getAttendanceHistory(
      String shipScheduleId, String userId, boolean isAdmin, boolean isSailor);
}
