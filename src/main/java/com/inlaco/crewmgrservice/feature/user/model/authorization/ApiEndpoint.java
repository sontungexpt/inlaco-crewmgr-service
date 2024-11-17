package com.inlaco.crewmgrservice.feature.user.model.authorization;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotNull;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.http.HttpMethod;

@Getter
@Setter
@Document(collection = "endpoints")
@JsonIgnoreProperties(
    value = {"id"},
    allowGetters = true)
public class ApiEndpoint {

  @Id private String id;

  @NotNull private String endpoint;

  @NotNull private HttpMethod method;

  private String displayName;

  private String description;

  @Override
  public int hashCode() {
    return Objects.hash(endpoint, method);
  }

  @Override
  public boolean equals(Object obj) {
    if (this == obj) return true;
    else if (!(obj instanceof ApiEndpoint)) return false;
    ApiEndpoint that = (ApiEndpoint) obj;
    return endpoint.equals(that.endpoint) && method.equals(that.method);
  }
}
