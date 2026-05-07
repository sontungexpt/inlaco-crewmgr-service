package com.inlaco.crewmgrservice.feature.otp.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.otp.domain.model.Otp;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "otp")
public class OtpEntity {

  @Id private String id;

  private String userId;

  private String recipient;

  private String otpCode;

  private Otp.OtpPurpose purpose;

  private String purposeId;

  private Otp.OtpSenderType senderType;

  private boolean isUsed;

  private Instant expiresAt;

  @CreatedDate private Instant createdAt;
}
