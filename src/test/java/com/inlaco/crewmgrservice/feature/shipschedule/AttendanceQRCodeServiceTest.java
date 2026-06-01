package com.inlaco.crewmgrservice.feature.shipschedule;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.AttendanceLogRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleCrewAssignmentRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.application.service.AttendanceQRCodeService;
import com.inlaco.crewmgrservice.feature.shipschedule.application.service.AttendanceQrTokenService;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.enums.CheckType;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.access.AccessDeniedException;

@ExtendWith(MockitoExtension.class)
class AttendanceQRCodeServiceTest {

  @Mock private AttendanceLogRepository attendanceLogRepository;
  @Mock private ShipScheduleRepository shipScheduleRepository;
  @Mock private ShipScheduleCrewAssignmentRepository assignmentRepository;
  @Mock private AttendanceQrTokenService qrTokenService;

  @InjectMocks private AttendanceQRCodeService service;

  @Test
  void adminShouldGetAllAttendanceLogsSortedNewestFirst() {
    AttendanceLog older = log("log-1", "crew-a", Instant.parse("2026-06-01T08:00:00Z"));
    AttendanceLog newer = log("log-2", "crew-b", Instant.parse("2026-06-01T09:00:00Z"));

    when(attendanceLogRepository.findByShipScheduleId("schedule-1"))
        .thenReturn(List.of(older, newer));

    List<AttendanceLog> result =
        service.getAttendanceHistory("schedule-1", "admin-1", true, false);

    assertEquals(List.of("log-2", "log-1"), result.stream().map(AttendanceLog::getId).toList());
    verify(attendanceLogRepository).findByShipScheduleId("schedule-1");
    verifyNoInteractions(shipScheduleRepository, assignmentRepository);
  }

  @Test
  void ownerShouldGetAllAttendanceLogsOfOwnedSchedule() {
    ShipSchedule schedule = ShipSchedule.builder().id("schedule-1").vesselOwnerId("owner-1").build();
    AttendanceLog older = log("log-1", "crew-a", Instant.parse("2026-06-01T08:00:00Z"));
    AttendanceLog newer = log("log-2", "crew-b", Instant.parse("2026-06-01T09:00:00Z"));

    when(shipScheduleRepository.findById("schedule-1")).thenReturn(Optional.of(schedule));
    when(attendanceLogRepository.findByShipScheduleId("schedule-1"))
        .thenReturn(List.of(older, newer));

    List<AttendanceLog> result =
        service.getAttendanceHistory("schedule-1", "owner-1", false, false);

    assertEquals(List.of("log-2", "log-1"), result.stream().map(AttendanceLog::getId).toList());
    verify(shipScheduleRepository).findById("schedule-1");
    verify(attendanceLogRepository).findByShipScheduleId("schedule-1");
  }

  @Test
  void sailorShouldGetOnlyOwnAttendanceLogsWhenAssigned() {
    ShipSchedule schedule = ShipSchedule.builder().id("schedule-1").vesselOwnerId("owner-1").build();
    ShipScheduleCrewAssignment assignment =
        ShipScheduleCrewAssignment.builder().scheduleId("schedule-1").accountId("sailor-1").build();
    AttendanceLog older = log("log-1", "sailor-1", Instant.parse("2026-06-01T08:00:00Z"));
    AttendanceLog newer = log("log-2", "sailor-1", Instant.parse("2026-06-01T09:00:00Z"));

    when(shipScheduleRepository.findById("schedule-1")).thenReturn(Optional.of(schedule));
    when(assignmentRepository.findByAccountIdAndScheduleId("sailor-1", "schedule-1"))
        .thenReturn(Optional.of(assignment));
    when(attendanceLogRepository.findByCrewIdAndShipScheduleId("sailor-1", "schedule-1"))
        .thenReturn(List.of(older, newer));

    List<AttendanceLog> result =
        service.getAttendanceHistory("schedule-1", "sailor-1", false, true);

    assertEquals(List.of("log-2", "log-1"), result.stream().map(AttendanceLog::getId).toList());
    verify(assignmentRepository).findByAccountIdAndScheduleId("sailor-1", "schedule-1");
    verify(attendanceLogRepository).findByCrewIdAndShipScheduleId("sailor-1", "schedule-1");
  }

  @Test
  void sailorShouldBeDeniedWhenNotAssignedToSchedule() {
    ShipSchedule schedule = ShipSchedule.builder().id("schedule-1").vesselOwnerId("owner-1").build();
    when(shipScheduleRepository.findById("schedule-1")).thenReturn(Optional.of(schedule));
    when(assignmentRepository.findByAccountIdAndScheduleId("sailor-1", "schedule-1"))
        .thenReturn(Optional.empty());

    assertThrows(
        AccessDeniedException.class,
        () -> service.getAttendanceHistory("schedule-1", "sailor-1", false, true));
  }

  @Test
  void ownerShouldBeDeniedWhenScheduleBelongsToAnotherUser() {
    ShipSchedule schedule =
        ShipSchedule.builder().id("schedule-1").vesselOwnerId("another-owner").build();
    when(shipScheduleRepository.findById("schedule-1")).thenReturn(Optional.of(schedule));

    assertThrows(
        AccessDeniedException.class,
        () -> service.getAttendanceHistory("schedule-1", "owner-1", false, false));
  }

  private AttendanceLog log(String id, String crewAccountId, Instant timestamp) {
    return AttendanceLog.builder()
        .id(id)
        .crewAccountId(crewAccountId)
        .shipScheduleId("schedule-1")
        .timestamp(timestamp)
        .checkType(CheckType.IN)
        .build();
  }
}
