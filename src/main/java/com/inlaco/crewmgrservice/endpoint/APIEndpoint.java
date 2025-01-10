package com.inlaco.crewmgrservice.endpoint;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.Objects;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpMethod;

@Getter
@Setter
@Builder
public class APIEndpoint {

  @Schema(description = "API Endpoint route" + " (e.g. /api/v1/users)")
  private String route;

  @Schema(description = "API Endpoint HTTP method")
  private HttpMethod method;

  @Schema(description = "API Endpoint display name")
  private String displayName;

  @Schema(description = "API Endpoint description")
  private String description;

  @Override
  public int hashCode() {
    return Objects.hash(route, method, displayName);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    else if (o instanceof APIEndpoint that) {
      return route.equals(that.route)
          && method.equals(that.method)
          && displayName.equals(that.displayName);
    }
    return false;
  }

  @Override
  public String toString() {
    return "APIEndpoint{"
        + "route='"
        + route
        + '\''
        + ", method="
        + method
        + ", displayName='"
        + displayName
        + '\''
        + ", description='"
        + description
        + '\''
        + '}';
  }
}
