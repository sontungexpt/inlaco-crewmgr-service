package com.inlaco.crewmgrservice.endpoint;

public enum APIEndpointCode {
  AUTH_01_V1("Login to the system"),
  AUTH_02_V1("Logout from the system"),
  AUTH_03_V1("Refresh the token"),
  AUTH_04_V1("Register to the system"),
  AUTH_05_V1("Forgot the password"),

  USER_01_V1("Change the password"),

  POST_01_V1("Create a new post"),
  POST_02_V1("Update the post"),
  POST_03_V1("Read the post"),
  POST_04_V1("Delete the post"),

  CONTRACT_01_V1("Fetch my contracts"),
  ;

  private final String description;

  APIEndpointCode(String description) {
    this.description = description;
  }

  public static APIEndpointCode from(String code) {
    for (APIEndpointCode name : APIEndpointCode.values()) {
      if (name.name().equals(code)) {
        return name;
      }
    }
    return null;
  }

  public String getDescription() {
    return description;
  }
}
