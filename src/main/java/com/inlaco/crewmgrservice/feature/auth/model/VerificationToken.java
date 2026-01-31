package com.inlaco.crewmgrservice.feature.auth.model;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.Id;

@Getter
@Setter
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode
public abstract class VerificationToken {

  @Id private String id;

  private String userId;

  @Default private Instant createdAt = Instant.now();

  protected VerificationToken(String userId) {
    this.userId = userId;
  }
}
