package com.inlaco.crewmgrservice.infrastructure.web.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class ValidationErrorResponse extends ErrorResponse<Map<String, String>> {

  public static ValidationErrorResponseBuilder<?, ?> of(HttpStatus status) {
    return new ValidationErrorResponseBuilderImpl().status(status);
  }

  public abstract static class ValidationErrorResponseBuilder<
          C extends ValidationErrorResponse, B extends ValidationErrorResponseBuilder<C, B>>
      extends ErrorResponseBuilder<Map<String, String>, C, B> {}
}
