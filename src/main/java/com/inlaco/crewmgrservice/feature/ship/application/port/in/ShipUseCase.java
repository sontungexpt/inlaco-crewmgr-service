package com.inlaco.crewmgrservice.feature.ship.application.port.in;

import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ShipUseCase {
  Ship createShip(Ship ship, User authenticatedUser);
  Ship updateShip(String shipId, Ship ship, User authenticatedUser);
  void deleteShip(String shipId, User authenticatedUser);
  Ship getShip(String shipId);
  Page<Ship> getShips(String search, String companyId, Pageable pageable);
  List<Ship> getActiveShips();
  List<Ship> getShipsByCompany(String companyId);
  Ship changeShipStatus(String shipId, Ship.ShipStatus status, User authenticatedUser);
  List<Ship> getAvailableShipsForScheduling();
  boolean isShipAvailableForScheduling(String shipId);
  void updateCrewCount(String shipId, int crewCount);
}
