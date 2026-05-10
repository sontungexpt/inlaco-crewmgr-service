package com.inlaco.crewmgrservice.shared.objectvalue;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ShipInfo {

  private String imoNumber;

  private String countryISO;

  private String name;

  private String description;

  private Asset image;

  private String type;
}
