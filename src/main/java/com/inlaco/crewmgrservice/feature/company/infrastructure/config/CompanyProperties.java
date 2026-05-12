package com.inlaco.crewmgrservice.feature.company.infrastructure.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "inlaco.company")
public class CompanyProperties {
  
  private String defaultStatus = "ACTIVE";
  
  private Validation validation = new Validation();
  
  @Data
  public static class Validation {
    private int maxNameLength = 200;
    private int maxDescriptionLength = 1000;
    private int maxRegistrationNumberLength = 50;
    private int maxTaxIdLength = 50;
    private int maxAddressLength = 500;
    private int maxEmailLength = 100;
    private int maxWebsiteLength = 200;
    private int maxPhoneNumberLength = 20;
  }
}
