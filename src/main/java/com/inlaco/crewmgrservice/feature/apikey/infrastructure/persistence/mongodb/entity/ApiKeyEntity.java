package com.inlaco.crewmgrservice.feature.apikey.infrastructure.persistence.mongodb.entity;

import com.inlaco.crewmgrservice.feature.apikey.domain.model.ApiKeyType;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "api_keys")
public class ApiKeyEntity {

  @Id private String id; // MongoDB will auto-generate this

  private String keyId;
  private String keySecret;
  private String clientName;
  private String description;
  private boolean active;
  private Instant expiresAt;
  private ApiKeyType type;

  @CreatedDate private Instant createdAt;
  @CreatedBy private String createdBy;
}
