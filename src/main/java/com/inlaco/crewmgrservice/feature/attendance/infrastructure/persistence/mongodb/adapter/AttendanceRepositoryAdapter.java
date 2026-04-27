package com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.attendance.application.port.out.AttendanceRepository;
import com.inlaco.crewmgrservice.feature.attendance.domain.model.Attendance;
import com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.entity.AttendanceEntity;
import com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.mapper.AttendanceEntityMapper;
import com.inlaco.crewmgrservice.feature.attendance.infrastructure.persistence.mongodb.repository.AttendanceMongoRepository;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttendanceRepositoryAdapter implements AttendanceRepository {

  private final AttendanceMongoRepository repository;
  private final AttendanceEntityMapper mapper;

  @Override
  public Optional<Attendance> findByUserIdAndScheduleId(String userId, String scheduleId) {
    return repository
        .findByUserIdAndScheduleId(new ObjectId(userId), new ObjectId(scheduleId))
        .map(mapper::toAttendance);
  }

  @Override
  public boolean existsByUserIdAndScheduleId(String userId, String scheduleId) {
    return repository.existsByUserIdAndScheduleId(new ObjectId(userId), new ObjectId(scheduleId));
  }

  @Override
  public Attendance save(Attendance attendance) {
    String id = attendance.getId();
    if (id == null) {
      return mapper.toAttendance(repository.insert(mapper.toAttendanceEntity(attendance)));
    } else {
      AttendanceEntity entity =
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
