package com.inlaco.crewmgrservice.endpoint;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "The friendly name of the endpoint code")
public enum APIEndpointName {
  AUTH_LOGIN(APIEndpointCode.AUTH_01_V1),

  AUTH_LOGOUT(APIEndpointCode.AUTH_02_V1),
  AUTH_REFRESH_TOKEN(APIEndpointCode.AUTH_03_V1),
  AUTH_REGISTER(APIEndpointCode.AUTH_04_V1),
  AUTH_FORGOT_PASSWORD(APIEndpointCode.AUTH_05_V1),

  USER_CHANGE_PASSWORD(APIEndpointCode.USER_01_V1),

  POST_CREATE(APIEndpointCode.POST_01_V1),
  POST_UPDATE(APIEndpointCode.POST_02_V1),
  POST_READ(APIEndpointCode.POST_03_V1),
  POST_DELETE(APIEndpointCode.POST_04_V1),
  ;

  private final APIEndpointCode code;

  public APIEndpointCode getCode() {
    return code;
  }

  APIEndpointName(APIEndpointCode code) {
    this.code = code;
  }

  public static APIEndpointName fromCode(APIEndpointCode code) {
    for (APIEndpointName name : APIEndpointName.values()) {
      if (name.getCode().equals(code)) {
        return name;
      }
    }
    return null;
  }

  public static APIEndpointName fromCode(String code) {
    for (APIEndpointName name : APIEndpointName.values()) {
      if (name.getCode().name().equals(code)) {
        return name;
      }
    }
    return null;
  }
}
