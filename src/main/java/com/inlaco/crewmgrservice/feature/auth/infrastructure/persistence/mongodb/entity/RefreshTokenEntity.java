package com.inlaco.crewmgrservice.feature.auth.infrastructure.persistence.mongodb.entity;

import java.time.Instant;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.Persistable;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "refresh_tokens")
public class RefreshTokenEntity implements Persistable<String> {

  @Id private String id;

  private String userPubId;

  @Indexed(unique = true)
  private String hashedToken;

  private Instant expiresAt;

  @Indexed(expireAfter = "10d") // automatically delete after 10 days of revocation
  private Instant revokedAt;

  @CreatedDate private Instant createdAt;

  @LastModifiedDate private Instant updatedAt;

  @Override
  public boolean isNew() {
    return createdAt == null || id == null;
  }
}
