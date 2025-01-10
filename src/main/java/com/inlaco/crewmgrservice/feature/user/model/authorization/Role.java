package com.inlaco.crewmgrservice.feature.user.model.authorization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import java.util.Set;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@JsonIgnoreProperties(
    value = {"id"},
    allowGetters = true)
@Builder
@Document(collection = "roles")
public class Role {

  @Id private String id;

  @Default private long version = 1;

  @Schema(description = "Role name", example = "ADMIN")
  @NotNull
  private String name;

  @Schema(description = "Role description", example = "Administrator")
  private String description;

  @DBRef
  @Schema(description = "Permissions that this right has", requiredMode = RequiredMode.NOT_REQUIRED)
  @Default
  private Set<Permission> permissions = Set.of();

  @Override
  public int hashCode() {
    return Objects.hash(id, name);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    else if (obj instanceof Role that) {
      return id.equals(that.id) && name.equals(that.name);
    }
    return false;
  }
}
