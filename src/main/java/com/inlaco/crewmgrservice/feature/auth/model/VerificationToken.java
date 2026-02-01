package com.inlaco.crewmgrservice.feature.auth.model;

import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@EqualsAndHashCode
public abstract class VerificationToken {

  @Id private String id;

  private String userId;

  private Instant createdAt = Instant.now();

  protected VerificationToken(String userId) {
    this.userId = userId;
  }
}
