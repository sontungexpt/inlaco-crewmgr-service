package com.inlaco.crewmgrservice.feature.apikey.domain.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Represents an API key used for authenticating external API requests.
 * 
 * <p>API keys provide a secure way for external systems to authenticate with the crew management service.
 * Each key has a unique identifier (keyId) and secret (keySecret), along with metadata about the client
 * and usage permissions.</p>
 * 
 * <p>Keys can be activated/deactivated and have optional expiration dates. The system supports different
 * key types with varying expiration policies and renewal capabilities.</p>
 * 
 * @author Crew Management Service
 * @version 1.0
 * @since 1.0
 */
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
  private ApiKeyType type;
  private String createdBy;

  /**
   * Checks if the API key is currently valid for authentication.
   * 
   * <p>A key is considered valid if it is active and not expired. This method should be called
   * before allowing any API operations with this key.</p>
   * 
   * @return true if the key is active and not expired, false otherwise
   */
  public boolean isValid() {
    return active && !isExpired();
  }

  /**
   * Checks if the API key has expired.
   * 
   * <p>A key is expired if it has an expiration date and that date is in the past.
   * Keys without expiration dates never expire.</p>
   * 
   * @return true if the key is expired, false otherwise
   */
  public boolean isExpired() {
    return expiresAt != null && expiresAt.isBefore(Instant.now());
  }

  /**
   * Deactivates the API key, making it invalid for authentication.
   * 
   * <p>Once deactivated, the key cannot be used for API requests until it is reactivated.
   * This operation is reversible.</p>
   */
  public void deactivate() {
    this.active = false;
  }

  /**
   * Activates the API key, making it valid for authentication (if not expired).
   * 
   * <p>This operation reactivates a previously deactivated key. The key must also
   * not be expired to be considered valid.</p>
   */
  public void activate() {
    this.active = true;
  }
}
