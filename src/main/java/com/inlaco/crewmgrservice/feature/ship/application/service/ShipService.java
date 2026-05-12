package com.inlaco.crewmgrservice.feature.ship.application.service;

import com.inlaco.crewmgrservice.feature.ship.application.model.ShipSearchCriteria;
import com.inlaco.crewmgrservice.feature.ship.application.port.in.ShipUseCase;
import com.inlaco.crewmgrservice.feature.ship.application.port.out.ShipRepository;
import com.inlaco.crewmgrservice.feature.ship.domain.error.ShipErrorCode;
import com.inlaco.crewmgrservice.feature.ship.domain.exception.ShipException;
import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
import com.inlaco.crewmgrservice.feature.upload.application.port.in.UploadDispatcher;
import com.inlaco.crewmgrservice.feature.upload.domain.enums.AssetType;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.shared.kernel.exception.ResourceNotFoundException;
import java.time.Instant;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Slf4j
public class ShipService implements ShipUseCase {

  private final ShipRepository shipRepository;
  private final UploadDispatcher uploadDispatcher;

  @Override
  public Ship createShip(Ship ship, User authenticatedUser) {
    log.debug("Creating ship with IMO number: {}", ship.getImoNumber());

    validateShipData(ship);
    checkDuplicateImoNumber(ship.getImoNumber());

    enrichShip(ship, authenticatedUser);

    Ship created = shipRepository.save(ship);
    log.info("Ship created successfully with ID: {}", created.getId());

    return created;
  }

  @Override
  public Ship updateShip(String shipId, Ship ship, User authenticatedUser) {
    log.debug("Updating ship with ID: {}", shipId);

    Ship existingShip = getShip(shipId);

    validateShipData(ship);
    
    if (!existingShip.getImoNumber().equals(ship.getImoNumber())) {
      checkDuplicateImoNumber(ship.getImoNumber());
    }

    updateShipFields(existingShip, ship, authenticatedUser);

    Ship updated = shipRepository.save(existingShip);
    log.info("Ship updated successfully with ID: {}", updated.getId());

    return updated;
  }

  @Override
  public void deleteShip(String shipId, User authenticatedUser) {
    log.debug("Deleting ship with ID: {}", shipId);

    Ship existingShip = getShip(shipId);
    
    // Business rule: Cannot delete active ships
    if (existingShip.getStatus() == Ship.ShipStatus.ACTIVE) {
      throw new ShipException(
          ShipErrorCode.INVALID_SHIP_STATUS,
          "Cannot delete active ship. Please deactivate it first.");
    }

    shipRepository.deleteById(shipId);
    log.info("Ship deleted successfully with ID: {}", shipId);
  }

  @Override
  public Ship getShip(String shipId) {
    log.debug("Fetching ship with ID: {}", shipId);
    return shipRepository
        .findById(shipId)
        .orElseThrow(() -> new ResourceNotFoundException(Ship.class, "id", shipId));
  }

  @Override
  public Page<Ship> getShips(String search, String companyId, Pageable pageable) {
    log.debug("Fetching ships with search: {}, companyId: {}", search, companyId);
    
    ShipSearchCriteria criteria = ShipSearchCriteria.builder()
        .name(search)
        .ownerCompanyId(companyId)
        .build();
    
    return shipRepository.findAll(criteria, pageable);
  }

  @Override
  public List<Ship> getActiveShips() {
    log.debug("Fetching active ships");
    return shipRepository.findByStatus(Ship.ShipStatus.ACTIVE);
  }

  @Override
  public List<Ship> getShipsByCompany(String companyId) {
    log.debug("Fetching ships for company: {}", companyId);
    return shipRepository.findByOwnerCompanyId(companyId);
  }

  @Override
  public Ship changeShipStatus(String shipId, Ship.ShipStatus status, User authenticatedUser) {
    log.debug("Changing status of ship {} to {}", shipId, status);

    Ship existingShip = getShip(shipId);
    existingShip.changeStatus(status);
    existingShip.setUpdatedBy(authenticatedUser.getId());
    existingShip.setUpdatedAt(Instant.now());

    Ship updated = shipRepository.save(existingShip);
    log.info("Ship status changed successfully to {} for ID: {}", status, updated.getId());

    return updated;
  }

  @Override
  public List<Ship> getAvailableShipsForScheduling() {
    log.debug("Fetching ships available for scheduling");
    return shipRepository.findAvailableShipsForScheduling();
  }

  @Override
  public boolean isShipAvailableForScheduling(String shipId) {
    log.debug("Checking if ship {} is available for scheduling", shipId);
    Ship ship = getShip(shipId);
    return ship.isAvailableForScheduling();
  }

  @Override
  public void updateCrewCount(String shipId, int crewCount) {
    log.debug("Updating crew count for ship {} to {}", shipId, crewCount);
    
    Ship ship = getShip(shipId);
    
    if (ship.getMaximumCrewCapacity() != null && crewCount > ship.getMaximumCrewCapacity()) {
      throw new ShipException(
          ShipErrorCode.CREW_CAPACITY_EXCEEDED,
          "Crew count exceeds maximum capacity of " + ship.getMaximumCrewCapacity());
    }
    
    shipRepository.updateCrewCount(shipId, crewCount);
    log.info("Crew count updated successfully for ship ID: {}", shipId);
  }

  private void validateShipData(Ship ship) {
    if (!StringUtils.hasText(ship.getName())) {
      throw new ShipException(ShipErrorCode.SHIP_NAME_REQUIRED, "Ship name is required");
    }

    if (!StringUtils.hasText(ship.getImoNumber())) {
      throw new ShipException(ShipErrorCode.IMO_NUMBER_REQUIRED, "IMO number is required");
    }

    if (!isValidImoNumber(ship.getImoNumber())) {
      throw new ShipException(ShipErrorCode.INVALID_IMO_NUMBER, "Invalid IMO number format");
    }
  }

  private void checkDuplicateImoNumber(String imoNumber) {
    if (shipRepository.existsByImoNumber(imoNumber)) {
      throw new ShipException(
          ShipErrorCode.SHIP_ALREADY_EXISTS,
          "Ship with IMO number " + imoNumber + " already exists");
    }
  }

  private void enrichShip(Ship ship, User authenticatedUser) {
    ship.setCreatedBy(authenticatedUser.getId());
    ship.setCreatedAt(Instant.now());
    ship.setUpdatedBy(authenticatedUser.getId());
    ship.setUpdatedAt(Instant.now());

    if (ship.getStatus() == null) {
      ship.setStatus(Ship.ShipStatus.ACTIVE);
    }

    if (ship.getCurrentCrewCount() == null) {
      ship.setCurrentCrewCount(0);
    }

    // Enrich image asset if present
    if (ship.getImage() != null) {
      ship.setImage(uploadDispatcher.enrich(AssetType.SHIP_IMAGE, ship.getImage()));
    }

    // Enrich documents asset if present
    if (ship.getDocuments() != null) {
      ship.setDocuments(uploadDispatcher.enrich(AssetType.SHIP_DOCUMENT, ship.getDocuments()));
    }
  }

  private void updateShipFields(Ship existingShip, Ship ship, User authenticatedUser) {
    existingShip.setName(ship.getName());
    existingShip.setImoNumber(ship.getImoNumber());
    existingShip.setCallSign(ship.getCallSign());
    existingShip.setMmsi(ship.getMmsi());
    existingShip.setFlag(ship.getFlag());
    existingShip.setPortOfRegistry(ship.getPortOfRegistry());
    existingShip.setShipType(ship.getShipType());
    existingShip.setClassificationSociety(ship.getClassificationSociety());
    existingShip.setYearBuilt(ship.getYearBuilt());
    existingShip.setShipyard(ship.getShipyard());
    existingShip.setDeadweight(ship.getDeadweight());
    existingShip.setGrossTonnage(ship.getGrossTonnage());
    existingShip.setNetTonnage(ship.getNetTonnage());
    existingShip.setLengthOverall(ship.getLengthOverall());
    existingShip.setBeam(ship.getBeam());
    existingShip.setDraft(ship.getDraft());
    existingShip.setEngineType(ship.getEngineType());
    existingShip.setEnginePower(ship.getEnginePower());
    existingShip.setFuelType(ship.getFuelType());
    existingShip.setMaximumCrewCapacity(ship.getMaximumCrewCapacity());
    existingShip.setOwnerCompanyId(ship.getOwnerCompanyId());
    existingShip.setOperatorCompanyId(ship.getOperatorCompanyId());
    existingShip.setDescription(ship.getDescription());
    existingShip.setLastInspectionDate(ship.getLastInspectionDate());
    existingShip.setNextInspectionDate(ship.getNextInspectionDate());

    if (ship.getImage() != null) {
      existingShip.setImage(uploadDispatcher.enrich(AssetType.SHIP_IMAGE, ship.getImage()));
    }

    if (ship.getDocuments() != null) {
      existingShip.setDocuments(uploadDispatcher.enrich(AssetType.SHIP_DOCUMENT, ship.getDocuments()));
    }

    existingShip.setUpdatedBy(authenticatedUser.getId());
    existingShip.setUpdatedAt(Instant.now());
  }

  private boolean isValidImoNumber(String imoNumber) {
    // IMO number validation: should be 7 digits followed by check digit
    return imoNumber != null && imoNumber.matches("^[0-9]{7}[0-9Xx]$");
  }
}
