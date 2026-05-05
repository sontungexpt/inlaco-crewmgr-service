package com.inlaco.crewmgrservice.feature.apikey.infrastructure.config;

import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
import java.time.Duration;
import java.time.Instant;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ApiKeyExpirationConfig {

  @Value("${apikey.expiration.ship-schedule:365d}")
  private Duration shipScheduleExpiration;

  @Value("${apikey.expiration.crew-management:180d}")
  private Duration crewManagementExpiration;

  @Value("${apikey.expiration.external:90d}")
  private Duration externalExpiration;

  @Value("${apikey.expiration.internal:30d}")
  private Duration internalExpiration;

  @Value("${apikey.expiration.default:365d}")
  private Duration defaultExpiration;

  public Instant calculateExpiration(ApiKeyType type) {
    Duration expiration = getExpirationForType(type);
    Instant expiresAt = Instant.now().plus(expiration);
    
    log.debug("Calculated expiration {} for API key type: {}", expiresAt, type);
    return expiresAt;
  }

  private Duration getExpirationForType(ApiKeyType type) {
    return switch (type) {
      case SHIP_SCHEDULE -> shipScheduleExpiration;
      case CREW_MANAGEMENT -> crewManagementExpiration;
      case EXTERNAL -> externalExpiration;
      case INTERNAL -> internalExpiration;
      default -> {
        log.warn("Unknown API key type: {}, using default expiration", type);
        yield defaultExpiration;
      }
    };
  }

  public Map<ApiKeyType, Duration> getAllExpirationTimes() {
    return Map.of(
        ApiKeyType.SHIP_SCHEDULE, shipScheduleExpiration,
        ApiKeyType.CREW_MANAGEMENT, crewManagementExpiration,
        ApiKeyType.EXTERNAL, externalExpiration,
        ApiKeyType.INTERNAL, internalExpiration
    );
  }
}
