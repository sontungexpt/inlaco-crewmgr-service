package com.inlaco.crewmgrservice.infrastructure.security.jwt.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@ConfigurationProperties(prefix = "token.access-token")
@Configuration
public class JwtProperties {

  private String secretKey;
  private long expiration;
}
