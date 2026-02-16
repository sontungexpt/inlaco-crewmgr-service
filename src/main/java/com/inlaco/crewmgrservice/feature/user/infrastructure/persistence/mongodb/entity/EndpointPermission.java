package com.inlaco.crewmgrservice.feature.user.infrastructure.persistence.mongodb.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.endpoint.APIEndpointName;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@JsonIgnoreProperties(
    value = {"id"},
    allowGetters = true)
@Document(collection = "permissions")
@Builder
@Schema(description = "Permission is a set of API endpoints that a user can access")
public class EndpointPermission {

  @Id private String id;

  @NotBlank
  @Indexed(unique = true)
  private String name;

  @Schema(description = "Permission display name", example = "Read")
  private String displayName;

  public String getDisplayName() {
    return displayName == null ? name : displayName;
  }

  @Schema(description = "Permission description", example = "Read permission")
  private String description;

  @DBRef
  @Schema(description = "API endpoints that this permission can access")
  private Set<@Valid APIEndpointName> apiEndpoints;

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    else if (obj instanceof EndpointPermission that) {
      return id.equals(that.id) && name.equals(that.name);
    }
    return false;
  }
}
