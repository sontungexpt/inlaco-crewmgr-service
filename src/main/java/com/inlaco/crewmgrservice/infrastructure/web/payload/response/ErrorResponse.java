package com.inlaco.crewmgrservice.infrastructure.web.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class ErrorResponse extends ApiResponse<Void> {

  private String errorCode;
  private String message;

  public static ErrorResponseBuilder<?, ?> of(HttpStatus status) {
    return new ErrorResponseBuilderImpl().status(status);
  }

  public abstract static class ErrorResponseBuilder<
          C extends ErrorResponse, B extends ErrorResponseBuilder<C, B>>
      extends ApiResponseBuilder<Void, C, B> {}
}
