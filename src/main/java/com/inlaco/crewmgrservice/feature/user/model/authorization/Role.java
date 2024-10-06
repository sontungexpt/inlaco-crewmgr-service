package com.inlaco.crewmgrservice.feature.user.model.authorization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.inlaco.crewmgrservice.feature.user.enums.RoleType;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Document(collection = "roles")
@JsonIgnoreProperties(
    value = {"id"},
    allowGetters = true)
@Builder
public class Role {
  @Id private String id;

  @NotNull private RoleType name;

  private String displayName;

  private String description;

  @Override
  public int hashCode() {
    return name.hashCode();
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    else if (!(obj instanceof Role)) return false;
    return name.equals(((Role) obj).name);
  }
}
