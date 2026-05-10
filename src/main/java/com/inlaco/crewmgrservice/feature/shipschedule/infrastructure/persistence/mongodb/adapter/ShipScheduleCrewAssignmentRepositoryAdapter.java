package com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.shipschedule.application.port.out.ShipScheduleCrewAssignmentRepository;
import com.inlaco.crewmgrservice.feature.shipschedule.domain.model.ShipScheduleCrewAssignment;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.entity.ShipScheduleCrewAssignmentEntity;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.mapper.ShipScheduleCrewAssignmentEntityMapper;
import com.inlaco.crewmgrservice.feature.shipschedule.infrastructure.persistence.mongodb.repository.ShipScheduleCrewAssignmentMongoRepository;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bson.types.ObjectId;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class ShipScheduleCrewAssignmentRepositoryAdapter
    implements ShipScheduleCrewAssignmentRepository {

  private final ShipScheduleCrewAssignmentEntityMapper mapper;
  private final ShipScheduleCrewAssignmentMongoRepository repository;

  @Override
  public ShipScheduleCrewAssignment save(ShipScheduleCrewAssignment shipScheduleCrewAssignment) {
    String id = shipScheduleCrewAssignment.getId();
    if (id == null) {
      return mapper.toShipScheduleCrewAssignment(
          repository.insert(mapper.toShipScheduleCrewAssignmentEntity(shipScheduleCrewAssignment)));
    }
    ShipScheduleCrewAssignmentEntity entity =
        repository
            .findById(id)
            .map(
                existing -> {
                  mapper.updateFromShipScheduleCrewAssignment(shipScheduleCrewAssignment, existing);
                  return existing;
                })
            .orElseGet(() -> mapper.toShipScheduleCrewAssignmentEntity(shipScheduleCrewAssignment));
    return mapper.toShipScheduleCrewAssignment(repository.save(entity));
  }

  @Override
  public List<ShipScheduleCrewAssignment> findByScheduleId(String shipScheduleId) {
    return repository.findByScheduleId(new ObjectId(shipScheduleId)).stream()
        .map(mapper::toShipScheduleCrewAssignment)
        .toList();
  }

  @Override
  public List<ShipScheduleCrewAssignment> findByProfileId(String crewId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'findByScheduleId'");
  }

  @Override
  public boolean existsByCrewIdAndShipScheduleId(String crewId, String shipScheduleId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
        "Unimplemented method 'existsByCrewIdAndShipScheduleId'");
  }

  @Override
  public Optional<ShipScheduleCrewAssignment> findByAccountIdAndScheduleId(
      String accountId, String shipScheduleId) {
    return repository
        .findByAccountIdAndScheduleId(new ObjectId(accountId), new ObjectId(shipScheduleId))
        .map(mapper::toShipScheduleCrewAssignment);
  }
}
