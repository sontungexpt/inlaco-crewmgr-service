package com.inlaco.crewmgrservice.feature.ship.application.model;

import com.inlaco.crewmgrservice.feature.ship.domain.model.Ship;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShipSearchCriteria {
  private String name;
  private String imoNumber;
  private String shipType;
  private Ship.ShipStatus status;
  private String ownerCompanyId;
  private String operatorCompanyId;
  private String flag;
}
