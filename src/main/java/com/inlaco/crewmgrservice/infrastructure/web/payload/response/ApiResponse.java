package com.inlaco.crewmgrservice.infrastructure.web.payload.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
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

  private int code;
  private T data;
  private String timestamp;
  private String cause;
  private String path;

  @JsonIgnore private HttpStatus status;

  public ResponseEntity<ApiResponse<T>> toResponseEntity() {
    return new ResponseEntity<>(this, this.status);
  }

  public static ApiResponseBuilder of(HttpStatus status) {
    var request = HttpServletUtils.getRequest().orElse(null);
    return new ApiResponseBuilderImpl()
        .status(status)
        .code(status.value())
        .path(request != null ? request.getRequestURI() : "")
        .timestamp(Instant.now().toString());
  }
}
