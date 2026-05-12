package com.inlaco.crewmgrservice.feature.ship.infrastructure.persistence.mongodb.adapter;

import com.inlaco.crewmgrservice.feature.ship.application.model.ShipSearchCriteria;
import com.inlaco.crewmgrservice.feature.ship.application.port.out.ShipRepository;
import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
import com.inlaco.crewmgrservice.feature.ship.infrastructure.persistence.mongodb.entity.ShipEntity;
import com.inlaco.crewmgrservice.feature.ship.infrastructure.persistence.mongodb.mapper.ShipEntityMapper;
import com.inlaco.crewmgrservice.feature.ship.infrastructure.persistence.mongodb.repository.ShipMongoRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
@Slf4j
public class ShipRepositoryAdapter implements ShipRepository {

  private final ShipMongoRepository mongoRepository;
  private final ShipEntityMapper entityMapper;
  private final MongoTemplate mongoTemplate;

  @Override
  public Ship save(Ship ship) {
    log.debug("Saving ship with IMO number: {}", ship.getImoNumber());
    ShipEntity entity = entityMapper.toEntity(ship);
    ShipEntity savedEntity = mongoRepository.save(entity);
    return entityMapper.toDomain(savedEntity);
  }

  @Override
  public Optional<Ship> findById(String id) {
    log.debug("Finding ship by ID: {}", id);
    return mongoRepository.findById(id)
        .map(entityMapper::toDomain);
  }

  @Override
  public Optional<Ship> findByImoNumber(String imoNumber) {
    log.debug("Finding ship by IMO number: {}", imoNumber);
    return mongoRepository.findByImoNumber(imoNumber)
        .map(entityMapper::toDomain);
  }

  @Override
  public Page<Ship> findAll(ShipSearchCriteria criteria, Pageable pageable) {
    log.debug("Finding ships with criteria: {}", criteria);
    var query = entityMapper.buildSearchQuery(criteria, pageable);
    var entities = mongoTemplate.find(query, ShipEntity.class);
    var countQuery = entityMapper.buildSearchQuery(criteria, null);
    var total = mongoTemplate.count(countQuery, ShipEntity.class);
    
    var shipEntities = entities.stream()
        .map(entityMapper::toDomain)
        .toList();
    
    return new org.springframework.data.domain.PageImpl<>(shipEntities, pageable, total);
  }

  @Override
  public List<Ship> findByStatus(Ship.ShipStatus status) {
    log.debug("Finding ships by status: {}", status);
    var entities = mongoRepository.findByStatus(status);
    return entityMapper.toDomainList(entities);
  }

  @Override
  public List<Ship> findByOwnerCompanyId(String companyId) {
    log.debug("Finding ships by owner company ID: {}", companyId);
    var entities = mongoRepository.findByOwnerCompanyId(companyId);
    return entityMapper.toDomainList(entities);
  }

  @Override
  public List<Ship> findByOperatorCompanyId(String companyId) {
    log.debug("Finding ships by operator company ID: {}", companyId);
    var entities = mongoRepository.findByOperatorCompanyId(companyId);
    return entityMapper.toDomainList(entities);
  }

  @Override
  public List<Ship> findAvailableShipsForScheduling() {
    log.debug("Finding ships available for scheduling");
    var entities = mongoRepository.findAvailableShipsForScheduling();
    return entityMapper.toDomainList(entities);
  }

  @Override
  public void deleteById(String id) {
    log.debug("Deleting ship by ID: {}", id);
    mongoRepository.deleteById(id);
  }

  @Override
  public boolean existsByImoNumber(String imoNumber) {
    log.debug("Checking if ship exists by IMO number: {}", imoNumber);
    return mongoRepository.existsByImoNumber(imoNumber);
  }

  @Override
  public void updateCrewCount(String shipId, int crewCount) {
    log.debug("Updating crew count for ship ID: {} to {}", shipId, crewCount);
    
    Query query = new Query(Criteria.where("_id").is(shipId));
    Update update = new Update().set("current_crew_count", crewCount);
    
    mongoTemplate.updateFirst(query, update, ShipEntity.class);
  }
}
