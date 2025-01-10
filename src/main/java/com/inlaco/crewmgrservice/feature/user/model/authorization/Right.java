package com.inlaco.crewmgrservice.feature.user.model.authorization;

import com.inlaco.crewmgrservice.endpoint.APIEndpointName;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.media.Schema.RequiredMode;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
@AllArgsConstructor
@Schema(description = "The main entity for the authorization")
public class Right {

  @DBRef
  @Schema(description = "Roles that this right has", requiredMode = RequiredMode.REQUIRED)
  private Set<Role> roles = Set.of();

  @Schema(description = "Included endpoints", requiredMode = RequiredMode.NOT_REQUIRED)
  private Set<APIEndpointName> includedEndpoints = Set.of();

  @Schema(description = "Excluded endpoints", requiredMode = RequiredMode.NOT_REQUIRED)
  private Set<APIEndpointName> excludedEndpoints = Set.of();
}
