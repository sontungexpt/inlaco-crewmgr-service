package com.inlaco.crewmgrservice.feature.ship.infrastructure.persistence.mongodb.mapper;

import com.inlaco.crewmgrservice.feature.ship.application.model.ShipSearchCriteria;
import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
import com.inlaco.crewmgrservice.feature.ship.infrastructure.persistence.mongodb.entity.ShipEntity;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ShipEntityMapper {

  private final MongoTemplate mongoTemplate;

  public ShipEntityMapper(MongoTemplate mongoTemplate) {
    this.mongoTemplate = mongoTemplate;
  }

  public Ship toDomain(ShipEntity entity) {
    if (entity == null) {
      return null;
    }

    return Ship.builder()
        .id(entity.getIdAsString())
        .name(entity.getName())
        .imoNumber(entity.getImoNumber())
        .callSign(entity.getCallSign())
        .mmsi(entity.getMmsi())
        .flag(entity.getFlag())
        .portOfRegistry(entity.getPortOfRegistry())
        .shipType(entity.getShipType())
        .classificationSociety(entity.getClassificationSociety())
        .yearBuilt(entity.getYearBuilt())
        .shipyard(entity.getShipyard())
        .deadweight(entity.getDeadweight())
        .grossTonnage(entity.getGrossTonnage())
        .netTonnage(entity.getNetTonnage())
        .lengthOverall(entity.getLengthOverall())
        .beam(entity.getBeam())
        .draft(entity.getDraft())
        .engineType(entity.getEngineType())
        .enginePower(entity.getEnginePower())
        .fuelType(entity.getFuelType())
        .maximumCrewCapacity(entity.getMaximumCrewCapacity())
        .currentCrewCount(entity.getCurrentCrewCount())
        .ownerCompanyId(entity.getOwnerCompanyId())
        .operatorCompanyId(entity.getOperatorCompanyId())
        .image(entity.getImage())
        .documents(entity.getDocuments())
        .description(entity.getDescription())
        .status(entity.getStatus())
        .lastInspectionDate(entity.getLastInspectionDate())
        .nextInspectionDate(entity.getNextInspectionDate())
        .createdBy(entity.getCreatedBy())
        .createdAt(entity.getCreatedAt() != null ? java.time.Instant.ofEpochSecond(entity.getCreatedAt()) : null)
        .updatedBy(entity.getUpdatedBy())
        .updatedAt(entity.getUpdatedAt() != null ? java.time.Instant.ofEpochSecond(entity.getUpdatedAt()) : null)
        .build();
  }

  public ShipEntity toEntity(Ship domain) {
    if (domain == null) {
      return null;
    }

    return ShipEntity.builder()
        .id(domain.getId() != null ? new ObjectId(domain.getId()) : null)
        .name(domain.getName())
        .imoNumber(domain.getImoNumber())
        .callSign(domain.getCallSign())
        .mmsi(domain.getMmsi())
        .flag(domain.getFlag())
        .portOfRegistry(domain.getPortOfRegistry())
        .shipType(domain.getShipType())
        .classificationSociety(domain.getClassificationSociety())
        .yearBuilt(domain.getYearBuilt())
        .shipyard(domain.getShipyard())
        .deadweight(domain.getDeadweight())
        .grossTonnage(domain.getGrossTonnage())
        .netTonnage(domain.getNetTonnage())
        .lengthOverall(domain.getLengthOverall())
        .beam(domain.getBeam())
        .draft(domain.getDraft())
        .engineType(domain.getEngineType())
        .enginePower(domain.getEnginePower())
        .fuelType(domain.getFuelType())
        .maximumCrewCapacity(domain.getMaximumCrewCapacity())
        .currentCrewCount(domain.getCurrentCrewCount())
        .ownerCompanyId(domain.getOwnerCompanyId())
        .operatorCompanyId(domain.getOperatorCompanyId())
        .image(domain.getImage())
        .documents(domain.getDocuments())
        .description(domain.getDescription())
        .status(domain.getStatus())
        .lastInspectionDate(domain.getLastInspectionDate())
        .nextInspectionDate(domain.getNextInspectionDate())
        .createdBy(domain.getCreatedBy())
        .createdAt(domain.getCreatedAt() != null ? domain.getCreatedAt().getEpochSecond() : null)
        .updatedBy(domain.getUpdatedBy())
        .updatedAt(domain.getUpdatedAt() != null ? domain.getUpdatedAt().getEpochSecond() : null)
        .build();
  }

  public List<Ship> toDomainList(List<ShipEntity> entities) {
    return entities.stream()
        .map(this::toDomain)
        .collect(Collectors.toList());
  }

  public Page<Ship> toDomainPage(Page<ShipEntity> entityPage) {
    List<Ship> ships = toDomainList(entityPage.getContent());
    return new PageImpl<>(ships, entityPage.getPageable(), entityPage.getTotalElements());
  }

  public Query buildSearchQuery(ShipSearchCriteria criteria, Pageable pageable) {
    Query query = new Query();

    if (criteria != null) {
      if (criteria.getName() != null && !criteria.getName().trim().isEmpty()) {
        query.addCriteria(Criteria.where("name").regex(criteria.getName(), "i"));
      }
      if (criteria.getImoNumber() != null && !criteria.getImoNumber().trim().isEmpty()) {
        query.addCriteria(Criteria.where("imo_number").is(criteria.getImoNumber()));
      }
      if (criteria.getShipType() != null && !criteria.getShipType().trim().isEmpty()) {
        query.addCriteria(Criteria.where("ship_type").regex(criteria.getShipType(), "i"));
      }
      if (criteria.getStatus() != null) {
        query.addCriteria(Criteria.where("status").is(criteria.getStatus()));
      }
      if (criteria.getOwnerCompanyId() != null && !criteria.getOwnerCompanyId().trim().isEmpty()) {
        query.addCriteria(Criteria.where("owner_company_id").is(criteria.getOwnerCompanyId()));
      }
      if (criteria.getOperatorCompanyId() != null && !criteria.getOperatorCompanyId().trim().isEmpty()) {
        query.addCriteria(Criteria.where("operator_company_id").is(criteria.getOperatorCompanyId()));
      }
      if (criteria.getFlag() != null && !criteria.getFlag().trim().isEmpty()) {
        query.addCriteria(Criteria.where("flag").regex(criteria.getFlag(), "i"));
      }
    }

    // Apply pagination
    if (pageable != null) {
      query.skip(pageable.getOffset());
      query.limit(pageable.getPageSize());
    }

    return query;
  }
}
