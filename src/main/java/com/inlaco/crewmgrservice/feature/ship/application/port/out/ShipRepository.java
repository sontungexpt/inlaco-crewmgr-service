package com.inlaco.crewmgrservice.feature.ship.application.port.out;

import com.inlaco.crewmgrservice.feature.ship.application.model.ShipSearchCriteria;
import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipRepository {
  Ship save(Ship ship);
  Optional<Ship> findById(String id);
  Optional<Ship> findByImoNumber(String imoNumber);
  Page<Ship> findAll(ShipSearchCriteria criteria, Pageable pageable);
  List<Ship> findByStatus(Ship.ShipStatus status);
  List<Ship> findByOwnerCompanyId(String companyId);
  List<Ship> findByOperatorCompanyId(String companyId);
  List<Ship> findAvailableShipsForScheduling();
  void deleteById(String id);
  boolean existsByImoNumber(String imoNumber);
  void updateCrewCount(String shipId, int crewCount);
}
