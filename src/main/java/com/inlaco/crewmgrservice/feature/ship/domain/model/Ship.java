package com.inlaco.crewmgrservice.feature.ship.domain.model;

import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents a maritime vessel in the crew management system.
 *
 * <p>Ships are the primary assets that require crew management. Each ship contains comprehensive
 * technical specifications, operational details, and status information necessary for crew
 * scheduling and management operations.
 *
 * <p>The ship status determines its availability for crew assignments and operations. Ships have
 * capacity constraints that must be respected when scheduling crew members.
 *
 * @author Trần Võ Sơn Tùng
 * @version 1.0
 * @since 1.0
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Ship {
  private String id;

  private String name;

  private String imoNumber;

  private String callSign;

  private String mmsi;

  private String flag;

  private String portOfRegistry;

  private String shipType;

  private String classificationSociety;

  private Integer yearBuilt;

  private String shipyard;

  private Double deadweight;

  private Double grossTonnage;

  private Double netTonnage;

  private Double lengthOverall;

  private Double beam;

  private Double draft;

  private String engineType;

  private Double enginePower;

  private String fuelType;

  private Integer maximumCrewCapacity;

  private Integer currentCrewCount;

  private String ownerCompanyId;

  private String operatorCompanyId;

  private Asset image;

  private Asset documents;

  private String description;

  private ShipStatus status;

  private LocalDate lastInspectionDate;

  private LocalDate nextInspectionDate;

  private String createdBy;

  private Instant createdAt;

  private String updatedBy;

  private Instant updatedAt;

  /** Represents the operational status of a ship. */
  public enum ShipStatus {
    /** Ship is active and available for crew assignments */
    ACTIVE,
    /** Ship is temporarily inactive and not available for assignments */
    INACTIVE,
    /** Ship is undergoing scheduled maintenance */
    UNDER_MAINTENANCE,
    /** Ship is under repair due to damage or malfunction */
    UNDER_REPAIR,
    /** Ship is permanently out of service */
    DECOMMISSIONED,
    /** Ship is listed for sale and not available for operations */
    FOR_SALE
  }

  /**
   * Changes the ship's status with business rule validation.
   *
   * <p>This method updates the ship's status after validating that the transition is allowed
   * according to business rules. Certain status transitions are prohibited to maintain data
   * integrity and operational safety.
   *
   * @param newStatus the new status to set
   * @throws IllegalStateException if the status transition is not allowed
   */
  public void changeStatus(ShipStatus newStatus) throws IllegalStateException {
    if (this.status == newStatus) return;

    // Add business rules for status transitions
    validateStatusTransition(newStatus);
    this.status = newStatus;
    this.updatedAt = Instant.now();
  }

  /**
   * Validates that the status transition is allowed according to business rules.
   *
   * @param newStatus the target status for transition
   * @throws IllegalStateException if the transition is not allowed
   */
  private void validateStatusTransition(ShipStatus newStatus) {
    // Business rule: Cannot change status from DECOMMISSIONED to ACTIVE
    if (this.status == ShipStatus.DECOMMISSIONED && newStatus == ShipStatus.ACTIVE) {
      throw new IllegalStateException(
          "Cannot reactivate a decommissioned ship. Please create a new ship record.");
    }

    // Business rule: Cannot change status to FOR_SALE if ship is ACTIVE
    if (this.status == ShipStatus.ACTIVE && newStatus == ShipStatus.FOR_SALE) {
      throw new IllegalStateException(
          "Cannot mark an active ship for sale. Please deactivate it first.");
    }
  }

  /**
   * Checks if the ship is available for crew scheduling.
   *
   * <p>A ship is available for scheduling only when it has an ACTIVE status. Other statuses
   * indicate the ship cannot accept crew assignments.
   *
   * @return true if the ship is available for scheduling, false otherwise
   */
  public boolean isAvailableForScheduling() {
    return status == ShipStatus.ACTIVE;
  }

  /**
   * Checks if the ship has sufficient crew capacity for additional crew members.
   *
   * <p>This method validates that adding the required number of crew members will not exceed the
   * ship's maximum crew capacity. Both current crew count and maximum capacity must be defined for
   * this check to be valid.
   *
   * @param requiredCrew the number of additional crew members needed
   * @return true if the ship has sufficient capacity, false otherwise
   */
  public boolean hasCrewCapacity(int requiredCrew) {
    return maximumCrewCapacity != null
        && currentCrewCount != null
        && (currentCrewCount + requiredCrew) <= maximumCrewCapacity;
  }
}
