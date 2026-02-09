package com.inlaco.crewmgrservice.feature.auth.domain.model;

import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@EqualsAndHashCode
public abstract class VerificationToken {

  private String id;

  private String userId;

  private Instant createdAt;

  protected VerificationToken(String userId) {
    this.userId = userId;
    this.createdAt = Instant.now();
  }
}
