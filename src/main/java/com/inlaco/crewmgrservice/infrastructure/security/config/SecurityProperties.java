package com.inlaco.crewmgrservice.infrastructure.security.config;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Slf4j
@ConfigurationProperties(prefix = "security")
public class SecurityProperties {

  private List<String> allowedCorsOrigins;

  @PostConstruct
  public void debug() {
    log.info("Allowed CORS origins: {}", allowedCorsOrigins);
  }
}
