package com.inlaco.crewmgrservice.feature.attendance.application.service;

import com.inlaco.crewmgrservice.feature.attendance.application.dto.CheckInRequest;
import com.inlaco.crewmgrservice.feature.attendance.application.dto.CheckOutRequest;
import com.inlaco.crewmgrservice.feature.attendance.application.port.in.CheckInUseCase;
import com.inlaco.crewmgrservice.feature.attendance.application.port.in.CheckOutUseCase;
import com.inlaco.crewmgrservice.feature.attendance.application.port.out.AttendanceRepository;
import com.inlaco.crewmgrservice.feature.attendance.domain.enums.AttendanceStatus;
import com.inlaco.crewmgrservice.feature.attendance.domain.model.Attendance;
import java.time.Instant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class AttendanceService implements CheckInUseCase, CheckOutUseCase {

  private final AttendanceRepository attendanceRepository;

  public Attendance checkIn(String userId, CheckInRequest request) {

    Attendance attendance =
        attendanceRepository
            .findByUserIdAndScheduleId(userId, request.getScheduleId())
            .orElse(null);

    if (attendance != null && attendance.getCheckInAt() != null) {
      throw new RuntimeException("Already checked in");
    }

    if (attendance == null) {
      attendance = Attendance.builder().userId(userId).scheduleId(request.getScheduleId()).build();
    }

    attendance.setCheckInAt(Instant.now());
    attendance.setCheckInMethod(request.getMethod());
    attendance.setLocation(request.getLocation());
    attendance.setStatus(AttendanceStatus.CHECKED_IN);

    return attendanceRepository.save(attendance);
  }

  public Attendance checkOut(String userId, CheckOutRequest request) {

    Attendance attendance =
        attendanceRepository
            .findByUserIdAndScheduleId(userId, request.getScheduleId())
            .orElseThrow(() -> new RuntimeException("Check-in not found"));

    if (attendance.getCheckOutAt() != null) {
      throw new RuntimeException("Already checked out");
    }

    attendance.setCheckOutAt(Instant.now());
    attendance.setCheckOutMethod(request.getMethod());
    attendance.setStatus(AttendanceStatus.CHECKED_OUT);

    return attendanceRepository.save(attendance);
  }
}
