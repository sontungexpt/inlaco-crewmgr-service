package com.inlaco.crewmgrservice.common.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inlaco.crewmgrservice.exceptions.BaseException;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class ExceptionResponse extends ApiResponse {
  private String message;
  private String exception;

  public static ExceptionResponseBuilder<?, ?> builder(BaseException ex) {
    return new ExceptionResponseBuilderImpl().baseException(ex);
  }

  public static ExceptionResponseBuilder<?, ?> builder(HttpStatus status) {
    return new ExceptionResponseBuilderImpl().status(status);
  }

  public static ExceptionResponseBuilder<?, ?> builder(Exception ex, HttpStatus status) {
    return builder(status)
        .exception(ex.getClass().getSimpleName())
        .message(ex.getMessage())
        .cause(ex.getCause() != null ? ex.getCause().getClass().getSimpleName() : null);
  }

  public abstract static class ExceptionResponseBuilder<
          C extends ExceptionResponse, B extends ExceptionResponseBuilder<C, B>>
      extends ApiResponseBuilder<C, B> {

    public B baseException(BaseException ex) {
      return self()
          .message(ex.getMessage())
          .status(ex.getStatus())
          .exception(ex.getClass().getSimpleName())
          .cause(ex.getCause() != null ? ex.getCause().getClass().getSimpleName() : null);
    }
  }
}
