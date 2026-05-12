package com.inlaco.crewmgrservice.feature.ship.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "inlaco.ship")
public class ShipProperties {
  
  private String defaultStatus = "ACTIVE";
  
  private Validation validation = new Validation();
  
  @Data
  public static class Validation {
    private int maxNameLength = 200;
    private int maxImoNumberLength = 20;
    private int maxCallSignLength = 20;
    private int maxMmsiLength = 20;
    private int maxFlagLength = 100;
    private int maxPortOfRegistryLength = 200;
    private int maxShipTypeLength = 100;
    private int maxClassificationSocietyLength = 100;
    private int maxShipyardLength = 200;
    private int maxEngineTypeLength = 100;
    private int maxFuelTypeLength = 50;
    private int maxDescriptionLength = 1000;
    private int minYearBuilt = 1800;
    private int maxDeadweight = 1000000;
    private int maxGrossTonnage = 1000000;
    private int maxNetTonnage = 1000000;
    private int maxLengthOverall = 500;
    private int maxBeam = 100;
    private int maxDraft = 50;
    private int maxEnginePower = 100000;
    private int maxCrewCapacity = 1000;
  }
}
