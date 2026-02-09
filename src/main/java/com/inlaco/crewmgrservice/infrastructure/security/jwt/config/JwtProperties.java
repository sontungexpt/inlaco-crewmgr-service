package com.inlaco.crewmgrservice.infrastructure.security.jwt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@ConfigurationProperties(prefix = "auth.access-token")
public class JwtProperties {

  private String secretKey;
  private long expiration;
}
