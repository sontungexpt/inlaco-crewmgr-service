package com.inlaco.crewmgrservice.feature.totp.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "totp_secrets")
public class TotpSecretEntity {

  @Id private String id;

  @Indexed private String userId;

  @Indexed private String email;

  private String secret;

  @Indexed private TotpSecret.TotpPurpose purpose;

  @Indexed private String purposeId;

  private Instant lastUsedAt;

  private int verificationCount;

  private boolean enabled;

  @CreatedDate private Instant createdAt;
}
