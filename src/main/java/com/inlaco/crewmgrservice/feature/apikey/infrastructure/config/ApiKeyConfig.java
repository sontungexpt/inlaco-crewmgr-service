package com.inlaco.crewmgrservice.feature.apikey.infrastructure.config;

import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
import java.time.Duration;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "apikey")
public class ApiKeyConfig {

  private boolean enabled;
  private Headers headers;
  private Security security;
  private Expiration expiration;

  // ===== HEADERS =====
  @Data
  public static class Headers {
    private String keyId;
    private String keySecret;
  }

  // ===== SECURITY =====
  @Data
  public static class Security {
    private boolean requireHttps;
  }

  // ===== EXPIRATION =====
  @Data
  public static class Expiration {
    private Duration defaultValue;
    private boolean defaultRenewable;
    private Map<ApiKeyType, ExpirationConfig> byType;
  }

  // ===== EXPIRATION CONFIG =====
  @Data
  @AllArgsConstructor
  public static class ExpirationConfig {
    private Duration duration;
    private boolean renewable;
  }
}
