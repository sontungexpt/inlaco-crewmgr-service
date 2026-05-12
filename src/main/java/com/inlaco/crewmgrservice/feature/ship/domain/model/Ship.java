package com.inlaco.crewmgrservice.feature.ship.domain.model;

import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

  public enum ShipStatus {
    ACTIVE,
    INACTIVE,
    UNDER_MAINTENANCE,
    UNDER_REPAIR,
    DECOMMISSIONED,
    FOR_SALE
  }

  public void changeStatus(ShipStatus newStatus) throws IllegalStateException {
    if (this.status == newStatus) return;
    
    // Add business rules for status transitions
    validateStatusTransition(newStatus);
    this.status = newStatus;
    this.updatedAt = Instant.now();
  }

  private void validateStatusTransition(ShipStatus newStatus) {
    // Business rule: Cannot change status from DECOMMISSIONED to ACTIVE
    if (this.status == ShipStatus.DECOMMISSIONED && newStatus == ShipStatus.ACTIVE) {
      throw new IllegalStateException("Cannot reactivate a decommissioned ship. Please create a new ship record.");
    }
    
    // Business rule: Cannot change status to FOR_SALE if ship is ACTIVE
    if (this.status == ShipStatus.ACTIVE && newStatus == ShipStatus.FOR_SALE) {
      throw new IllegalStateException("Cannot mark an active ship for sale. Please deactivate it first.");
    }
  }

  public boolean isAvailableForScheduling() {
    return status == ShipStatus.ACTIVE;
  }

  public boolean hasCrewCapacity(int requiredCrew) {
    return maximumCrewCapacity != null && 
           currentCrewCount != null && 
           (currentCrewCount + requiredCrew) <= maximumCrewCapacity;
  }
}
