package com.inlaco.crewmgrservice.infrastructure.web.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inlaco.crewmgrservice.infrastructure.web.util.HttpServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class ApiResponse<T> {

  private HttpStatus status;
  private String timestamp;
  private String path;
  private T data;

  // return code from http status to client
  public int getCode() {
    return status.value();
  }

  public ResponseEntity<ApiResponse<T>> toResponseEntity() {
    return new ResponseEntity<>(this, this.status);
  }

  public static <T> ApiResponseBuilder<T, ?, ?> of(HttpStatus status) {
    return new ApiResponseBuilderImpl<T>()
        .status(status)
        .path(HttpServletUtils.getRequest().map(HttpServletRequest::getRequestURI).orElse(""))
        .timestamp(Instant.now().toString());
  }
}
