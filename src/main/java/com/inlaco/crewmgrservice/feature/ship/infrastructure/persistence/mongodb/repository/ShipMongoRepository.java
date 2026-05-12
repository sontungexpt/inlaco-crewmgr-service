package com.inlaco.crewmgrservice.feature.ship.infrastructure.persistence.mongodb.repository;

import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
import com.inlaco.crewmgrservice.feature.ship.infrastructure.persistence.mongodb.entity.ShipEntity;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface ShipMongoRepository extends MongoRepository<ShipEntity, String> {

  Optional<ShipEntity> findByImoNumber(String imoNumber);

  boolean existsByImoNumber(String imoNumber);

  @Query("{ 'name': { $regex: ?0, $options: 'i' } }")
  List<ShipEntity> findByNameContainingIgnoreCase(String name);

  @Query("{ 'status': ?0 }")
  List<ShipEntity> findByStatus(Ship.ShipStatus status);

  @Query("{ 'owner_company_id': ?0 }")
  List<ShipEntity> findByOwnerCompanyId(String companyId);

  @Query("{ 'operator_company_id': ?0 }")
  List<ShipEntity> findByOperatorCompanyId(String companyId);

  @Query("{ 'status': 'ACTIVE' }")
  List<ShipEntity> findAvailableShipsForScheduling();

  }
