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
public class ValidationErrorResponse extends ApiResponse<Map<String, String>> {

  private String errorCode;
  private String message;

  public static ValidationErrorResponseBuilder<?, ?> of(HttpStatus status) {
    return new ValidationErrorResponseBuilderImpl().status(status);
  }

  public abstract static class ValidationErrorResponseBuilder<
          C extends ValidationErrorResponse, B extends ValidationErrorResponseBuilder<C, B>>
      extends ApiResponseBuilder<Map<String, String>, C, B> {}
}
