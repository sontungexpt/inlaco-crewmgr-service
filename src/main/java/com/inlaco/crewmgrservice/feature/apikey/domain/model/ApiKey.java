package com.inlaco.crewmgrservice.feature.apikey.domain.model;

import com.aventrix.jnanoid.jnanoid.NanoIdUtils;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ApiKey {
  private String id;
  private String keyId;
  private String keySecret;
  private String clientName;
  private String description;
  private boolean active;
  private Instant createdAt;
  private Instant expiresAt;
  private String createdBy;
  private ApiKeyType type;

  public static ApiKey generateNew(
      String clientName, String description, String createdBy, ApiKeyType type, Instant expiresAt) {
    String keyId = "sk_" + NanoIdUtils.randomNanoId();
    String keySecret =
        NanoIdUtils.randomNanoId(
            NanoIdUtils.DEFAULT_NUMBER_GENERATOR,
            NanoIdUtils.DEFAULT_ALPHABET,
            NanoIdUtils.DEFAULT_SIZE * 2);

    return ApiKey.builder()
        .keyId(keyId)
        .keySecret(keySecret)
        .clientName(clientName)
        .description(description)
        .active(true)
        .createdAt(Instant.now())
        .expiresAt(expiresAt)
        .createdBy(createdBy)
        .type(type)
        .build();
  }

  public static ApiKey generateNew(String clientName, String description, String createdBy) {
    return generateNew(clientName, description, createdBy, ApiKeyType.EXTERNAL, null);
  }

  public boolean isValid() {
    return active && !isExpired();
  }

  public boolean isExpired() {
    return expiresAt != null && expiresAt.isBefore(Instant.now());
  }

  public void deactivate() {
    this.active = false;
  }

  public void activate() {
    this.active = true;
  }
}
