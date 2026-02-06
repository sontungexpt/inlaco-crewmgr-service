package com.inlaco.crewmgrservice.infrastructure.web.payload.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inlaco.crewmgrservice.utils.HttpServletUtils;
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
  private String cause;
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
    var request = HttpServletUtils.getRequest().orElse(null);
    return new ApiResponseBuilderImpl<T>()
        .status(status)
        .path(request != null ? request.getRequestURI() : "")
        .timestamp(Instant.now().toString());
  }
}
