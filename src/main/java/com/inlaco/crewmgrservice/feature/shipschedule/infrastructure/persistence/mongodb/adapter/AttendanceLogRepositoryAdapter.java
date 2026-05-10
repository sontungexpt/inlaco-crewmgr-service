package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.AttendanceLogRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.AttendanceLog;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.AttendanceLogEntity;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.mapper.AttendanceLogEntityMapper;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository.AttendanceLogMongoRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class AttendanceLogRepositoryAdapter implements AttendanceLogRepository {

  private final AttendanceLogMongoRepository mongoRepository;

  private final AttendanceLogEntityMapper entityMapper;

  @Override
  public AttendanceLog save(AttendanceLog attendanceLog) {

    AttendanceLogEntity entity = entityMapper.toEntity(attendanceLog);

    AttendanceLogEntity saved = mongoRepository.save(entity);

    return entityMapper.toDomain(saved);
  }

  @Override
  public Optional<AttendanceLog> findById(String id) {

    return mongoRepository.findById(id).map(entityMapper::toDomain);
  }

  @Override
  public List<AttendanceLog> findByCrewId(String crewId) {

    return mongoRepository.findByCrewAccountId(crewId).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<AttendanceLog> findByShipScheduleId(String shipScheduleId) {

    return mongoRepository.findByShipScheduleId(shipScheduleId).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<AttendanceLog> findByCrewIdAndShipScheduleId(String crewId, String shipScheduleId) {

    return mongoRepository.findByCrewAccountIdAndShipScheduleId(crewId, shipScheduleId).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<AttendanceLog> findByTimestampBetween(Instant startTime, Instant endTime) {

    return mongoRepository.findByTimestampBetween(startTime, endTime).stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<AttendanceLog> findByCrewIdAndTimestampBetween(
      String crewId, Instant startTime, Instant endTime) {

    return mongoRepository
        .findByCrewAccountIdAndTimestampBetween(crewId, startTime, endTime)
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public List<AttendanceLog> findByShipScheduleIdAndTimestampBetween(
      String shipScheduleId, Instant startTime, Instant endTime) {

    return mongoRepository
        .findByShipScheduleIdAndTimestampBetween(shipScheduleId, startTime, endTime)
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }

  @Override
  public Optional<AttendanceLog> findLatestByEmployeeCardIdAndShipScheduleId(
      String employeeCardId, String shipScheduleId) {

    return mongoRepository
        .findTopByCrewEmployeeCardIdAndShipScheduleIdOrderByTimestampDesc(
            employeeCardId, shipScheduleId)
        .map(entityMapper::toDomain);
  }

  @Override
  public Page<AttendanceLog> findAll(Pageable pageable) {

    return mongoRepository.findAll(pageable).map(entityMapper::toDomain);
  }

  @Override
  public void deleteById(String id) {
    mongoRepository.deleteById(id);
  }

  @Override
  public boolean existsById(String id) {
    return mongoRepository.existsById(id);
  }
}
