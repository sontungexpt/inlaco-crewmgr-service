package com.inlaco.crewmgrservice.feature.ship.presentation.rest.dto.response;

import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
import com.inlaco.crewmgrservice.shared.objectvalue.AssetResponse;
import java.time.Instant;
import java.time.LocalDate;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ShipResponse {

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
  private AssetResponse image;
  private AssetResponse documents;
  private String description;
  private Ship.ShipStatus status;
  private LocalDate lastInspectionDate;
  private LocalDate nextInspectionDate;
  private String createdBy;
  private String updatedBy;
  private Instant createdAt;
  private Instant updatedAt;
}
