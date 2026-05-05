package com.inlaco.crewmgrservice.feature.totp.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.totp.domain.model.TotpSecret;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.index.Indexed;

import java.time.Instant;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "totp_secrets")
public class TotpSecretEntity {

  @Id
  private String id;
  
  @Indexed
  private String userId;
  
  @Indexed
  private String email;
  
  private String secret;
  
  @Indexed
  private TotpSecret.TotpPurpose purpose;
  
  @Indexed
  private String purposeId;
  
  private boolean enabled;
  private Instant createdAt;
  private Instant lastUsedAt;
  private int verificationCount;

  public static TotpSecretEntity fromDomain(TotpSecret totpSecret) {
    return TotpSecretEntity.builder()
        .id(totpSecret.getId())
        .userId(totpSecret.getUserId())
        .email(totpSecret.getEmail())
        .secret(totpSecret.getSecret())
        .purpose(totpSecret.getPurpose())
        .purposeId(totpSecret.getPurposeId())
        .enabled(totpSecret.isEnabled())
        .createdAt(totpSecret.getCreatedAt())
        .lastUsedAt(totpSecret.getLastUsedAt())
        .verificationCount(totpSecret.getVerificationCount())
        .build();
  }

  public TotpSecret toDomain() {
    return TotpSecret.builder()
        .id(id)
        .userId(userId)
        .email(email)
        .secret(secret)
        .purpose(purpose)
        .purposeId(purposeId)
        .enabled(enabled)
        .createdAt(createdAt)
        .lastUsedAt(lastUsedAt)
        .verificationCount(verificationCount)
        .build();
  }
}
