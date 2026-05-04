package com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.attendance.application.port.out.AttendanceRepository;
import com.inlaco.crewmgrservice.feature.attendance.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.entity.AttendanceLogEntity;
import com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.mapper.AttendanceEntityMapper;
import com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.repository.AttendanceLogMongoRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryAdapter implements AttendanceRepository {

  private final AttendanceLogMongoRepository repository;
  private final AttendanceEntityMapper mapper;

  @Override
  public Optional<AttendanceLog> findByUserIdAndScheduleId(String userId, String scheduleId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public boolean existsByUserIdAndScheduleId(String userId, String scheduleId) {
    throw new UnsupportedOperationException();
  }

  @Override
  public AttendanceLog save(AttendanceLog attendance) {
    String id = attendance.getId();
    if (id == null) {
      return mapper.toAttendance(repository.insert(mapper.toAttendanceEntity(attendance)));
    } else {
      AttendanceLogEntity entity =
          repository
              .findById(id)
              .map(
                  existing -> {
                    mapper.updateFromAttendance(attendance, existing);
                    return existing;
                  })
              .orElseGet(() -> mapper.toAttendanceEntity(attendance));
      return mapper.toAttendance(repository.save(entity));
    }
  }
}
