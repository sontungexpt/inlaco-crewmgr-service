package com.inlaco.crewmgrservice.feature.attendance.application.service;

import com.inlaco.crewmgrservice.feature.attendance.application.dto.CheckInCommand;
import com.inlaco.crewmgrservice.feature.attendance.application.port.in.CheckInUseCase;
import com.inlaco.crewmgrservice.feature.attendance.domain.model.AttendanceLog;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CheckInService implements CheckInUseCase {
  @Override
  public AttendanceLog checkIn(CheckInCommand request) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'checkIn'");
  }
  // private final AttendanceDomainService domainService;
  // private final QrService qrService;
  // private final ScheduleService scheduleService;
  // public AttendanceLog checkIn(CheckInCommand command) {
  //   QrPayload payload = qrService.verify(command.getQrToken());
  //   String personId = payload.getPersonId();
  //   String scheduleId = payload.getScheduleId();
  //   String companyId = payload.getCompanyId();
  //   if (!scheduleService.isPersonAssigned(scheduleId, personId)) {
  //     throw new IllegalArgumentException("Person not assigned to schedule");
  //   }
  //   domainService.validateNoDuplicateCheckIn(scheduleId, personId);
  //   AttendanceLog log =
  //       AttendanceLog.createCheckIn(
  //           scheduleId, personId, companyId, command.getLocation(), command.getDeviceId());
  //   AttendanceLog saved = domainService.save(log);
  //   return new AttendanceResponse(
  //       saved.getId(),
  //       saved.getScheduleId(),
  //       saved.getPersonId(),
  //       saved.getTimestamp(),
  //       "CHECKED_IN");
  // }
}
