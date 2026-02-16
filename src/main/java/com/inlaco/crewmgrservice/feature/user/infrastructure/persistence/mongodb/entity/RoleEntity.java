package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.entity;

import java.time.Instant;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@EqualsAndHashCode(of = "id")
@Document(collection = "roles")
public class RoleEntity {

  @Id private String id;

  @Indexed(unique = true)
  private String name;

  private String description;

  @CreatedDate private Instant createdAt;

  @CreatedBy private ObjectId createdBy;

  @LastModifiedDate private Instant updatedAt;

  @LastModifiedBy private String updatedBy;

  // @DBRef
  // @Schema(description = "Permissions that this right has", requiredMode =
  // RequiredMode.NOT_REQUIRED)
  // @Default
  // private Set<EndpointPermission> permissions = Set.of();
}
