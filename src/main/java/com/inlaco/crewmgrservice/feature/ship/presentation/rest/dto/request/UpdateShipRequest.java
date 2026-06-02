package com.inlaco.crewmgrservice.feature.ship.presentation.rest.dto.request;

import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
import com.inlaco.crewmgrservice.shared.objectvalue.Asset;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateShipRequest {

  @Size(max = 200, message = "Ship name must not exceed 200 characters")
  private String name;

  @Pattern(regexp = "^[0-9]{7}[0-9Xx]$", message = "Invalid IMO number format")
  private String imoNumber;

  @Size(max = 20, message = "Call sign must not exceed 20 characters")
  private String callSign;

  @Size(max = 20, message = "MMSI must not exceed 20 characters")
  private String mmsi;

  @Size(max = 100, message = "Flag must not exceed 100 characters")
  private String flag;

  @Size(max = 200, message = "Port of registry must not exceed 200 characters")
  private String portOfRegistry;

  @Size(max = 100, message = "Ship type must not exceed 100 characters")
  private String shipType;

  @Size(max = 100, message = "Classification society must not exceed 100 characters")
  private String classificationSociety;

  @Min(value = 1800, message = "Year built must be after 1800")
  private Integer yearBuilt;

  @Size(max = 200, message = "Shipyard must not exceed 200 characters")
  private String shipyard;

  @Min(value = 0, message = "Deadweight must be positive")
  private Double deadweight;

  @Min(value = 0, message = "Gross tonnage must be positive")
  private Double grossTonnage;

  @Min(value = 0, message = "Net tonnage must be positive")
  private Double netTonnage;

  @Min(value = 0, message = "Length overall must be positive")
  private Double lengthOverall;

  @Min(value = 0, message = "Beam must be positive")
  private Double beam;

  @Min(value = 0, message = "Draft must be positive")
  private Double draft;

  @Size(max = 100, message = "Engine type must not exceed 100 characters")
  private String engineType;

  @Min(value = 0, message = "Engine power must be positive")
  private Double enginePower;

  @Size(max = 50, message = "Fuel type must not exceed 50 characters")
  private String fuelType;

  @Min(value = 0, message = "Maximum crew capacity must be positive")
  private Integer maximumCrewCapacity;

  private String ownerCompanyId;

  private String operatorCompanyId;

  private Asset image;

  private Asset documents;

  @Size(max = 1000, message = "Description must not exceed 1000 characters")
  private String description;

  private LocalDate lastInspectionDate;

  private LocalDate nextInspectionDate;

  private Ship.ShipStatus status;
}
