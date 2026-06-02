package com.inlaco.crewmgrservice.feature.apikey.domain.model;

public enum ApiKeyType {
  SHIP_SCHEDULE("ship_schedule", "Ship Schedule API Key"),
  CREW_MANAGEMENT("crew_management", "Crew Management API Key"),
  EXTERNAL("external", "External API Key"),
  INTERNAL("internal", "Internal API Key");

  private final String code;
  private final String description;

  ApiKeyType(String code, String description) {
    this.code = code;
    this.description = description;
  }

  public String getCode() {
    return code;
  }

  public String getDescription() {
    return description;
  }
}
