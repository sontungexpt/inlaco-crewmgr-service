package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipSchedule;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleEntity;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.mapper.ShipScheduleEntityMapper;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository.ShipScheduleMongoRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class ShipScheduleRepositoryAdapter implements ShipScheduleRepository {
  
  private final ShipScheduleMongoRepository mongoRepository;
  private final ShipScheduleEntityMapper entityMapper;
  
  @Override
  public ShipSchedule save(ShipSchedule shipSchedule) {
    ShipScheduleEntity entity = entityMapper.toEntity(shipSchedule);
    ShipScheduleEntity saved = mongoRepository.save(entity);
    return entityMapper.toDomain(saved);
  }
  
  @Override
  public Optional<ShipSchedule> findById(String id) {
    return mongoRepository.findById(id)
        .map(entityMapper::toDomain);
  }
  
  @Override
  public List<ShipSchedule> findByClientId(String clientId) {
    return mongoRepository.findByClientId(clientId)
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }
  
  @Override
  public List<ShipSchedule> findByShipId(String shipId) {
    return mongoRepository.findByShipId(shipId)
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }
  
  @Override
  public List<ShipSchedule> findByDepartureTimeBetween(Instant startTime, Instant endTime) {
    return mongoRepository.findByDepartureTimeBetween(startTime, endTime)
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }
  
  @Override
  public List<ShipSchedule> findByStatus(String status) {
    return mongoRepository.findByStatus(status)
        .stream()
        .map(entityMapper::toDomain)
        .toList();
  }
  
  @Override
  public List<ShipSchedule> findAll() {
    return mongoRepository.findAll()
        .stream()
        .map(entityMapper::toDomain)
        .toList();
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
