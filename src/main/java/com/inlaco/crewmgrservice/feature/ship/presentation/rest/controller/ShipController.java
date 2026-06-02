package com.inlaco.crewmgrservice.feature.ship.presentation.rest.controller;

import com.inlaco.crewmgrservice.feature.ship.application.port.in.ShipUseCase;
import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
import com.inlaco.crewmgrservice.feature.ship.presentation.mapper.ShipMapper;
import com.inlaco.crewmgrservice.feature.ship.presentation.rest.dto.request.CreateShipRequest;
import com.inlaco.crewmgrservice.feature.ship.presentation.rest.dto.request.UpdateShipRequest;
import com.inlaco.crewmgrservice.feature.ship.presentation.rest.dto.response.ShipResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/ships")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Ship Management", description = "API for managing ships")
public class ShipController {

  private final ShipUseCase shipUseCase;
  private final ShipMapper shipMapper;

  @PostMapping
  @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY_ADMIN')")
  @Operation(summary = "Create a new ship")
  public ResponseEntity<ShipResponse> createShip(@Valid @RequestBody CreateShipRequest request) {
    log.info("Creating ship with IMO number: {}", request.getImoNumber());

    Ship ship = shipMapper.toDomain(request);
    Ship created = shipUseCase.createShip(ship, null);

    return ResponseEntity.status(HttpStatus.CREATED).body(shipMapper.toResponse(created));
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY_ADMIN')")
  @Operation(summary = "Update an existing ship")
  public ResponseEntity<ShipResponse> updateShip(
      @PathVariable String id, @Valid @RequestBody UpdateShipRequest request) {
    log.info("Updating ship with ID: {}", id);

    Ship ship = shipMapper.toDomain(request);
    Ship updated = shipUseCase.updateShip(id, ship, null);

    return ResponseEntity.ok(shipMapper.toResponse(updated));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY_ADMIN')")
  @Operation(summary = "Delete a ship")
  public ResponseEntity<Void> deleteShip(@PathVariable String id) {
    log.info("Deleting ship with ID: {}", id);
    shipUseCase.deleteShip(id, null);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/{id}")
  @Operation(summary = "Get ship by ID")
  public ResponseEntity<ShipResponse> getShip(@PathVariable String id) {
    log.debug("Fetching ship with ID: {}", id);
    Ship ship = shipUseCase.getShip(id);
    return ResponseEntity.ok(shipMapper.toResponse(ship));
  }

  @GetMapping
  @Operation(summary = "Get all ships with pagination")
  public ResponseEntity<Page<ShipResponse>> getShips(
      @Parameter(description = "Search term for ship name") @RequestParam(required = false)
          String search,
      @Parameter(description = "Company ID to filter ships by owner")
          @RequestParam(required = false)
          String companyId,
      Pageable pageable) {
    log.debug("Fetching ships with search: {}, companyId: {}", search, companyId);

    Page<Ship> ships = shipUseCase.getShips(search, companyId, pageable);
    Page<ShipResponse> responses = ships.map(shipMapper::toResponse);

    return ResponseEntity.ok(responses);
  }

  @GetMapping("/active")
  @Operation(summary = "Get all active ships")
  public ResponseEntity<List<ShipResponse>> getActiveShips() {
    log.debug("Fetching active ships");
    List<Ship> ships = shipUseCase.getActiveShips();
    List<ShipResponse> responses = ships.stream().map(shipMapper::toResponse).toList();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/available")
  @Operation(summary = "Get ships available for scheduling")
  public ResponseEntity<List<ShipResponse>> getAvailableShips() {
    log.debug("Fetching ships available for scheduling");
    List<Ship> ships = shipUseCase.getAvailableShipsForScheduling();
    List<ShipResponse> responses = ships.stream().map(shipMapper::toResponse).toList();
    return ResponseEntity.ok(responses);
  }

  @GetMapping("/company/{companyId}")
  @Operation(summary = "Get ships by company")
  public ResponseEntity<List<ShipResponse>> getShipsByCompany(@PathVariable String companyId) {
    log.debug("Fetching ships for company: {}", companyId);
    List<Ship> ships = shipUseCase.getShipsByCompany(companyId);
    List<ShipResponse> responses = ships.stream().map(shipMapper::toResponse).toList();
    return ResponseEntity.ok(responses);
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY_ADMIN')")
  @Operation(summary = "Change ship status")
  public ResponseEntity<ShipResponse> changeShipStatus(
      @PathVariable String id, @RequestParam Ship.ShipStatus status) {
    log.info("Changing status of ship {} to {}", id, status);

    Ship updated = shipUseCase.changeShipStatus(id, status, null);
    return ResponseEntity.ok(shipMapper.toResponse(updated));
  }

  @PatchMapping("/{id}/crew-count")
  @PreAuthorize("hasRole('ADMIN') or hasRole('COMPANY_ADMIN')")
  @Operation(summary = "Update ship crew count")
  public ResponseEntity<Void> updateCrewCount(
      @PathVariable String id, @RequestParam int crewCount) {
    log.info("Updating crew count for ship {} to {}", id, crewCount);
    shipUseCase.updateCrewCount(id, crewCount);
    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}/availability")
  @Operation(summary = "Check if ship is available for scheduling")
  public ResponseEntity<Boolean> checkShipAvailability(@PathVariable String id) {
    log.debug("Checking availability for ship: {}", id);
    boolean isAvailable = shipUseCase.isShipAvailableForScheduling(id);
    return ResponseEntity.ok(isAvailable);
  }
}
