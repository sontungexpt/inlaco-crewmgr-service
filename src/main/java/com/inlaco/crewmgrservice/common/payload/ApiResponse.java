package com.inlaco.crewmgrservice.common.payload;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.inlaco.crewmgrservice.utils.HttpServletUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.SuperBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@Getter
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@SuperBuilder
public class ApiResponse {

  private int code;
  private Object data;
  private String timestamp;
  private String cause;
  private String path;

  public String getPath() {
    if (path == null) {
      var request = HttpServletUtils.getRequest().orElse(null);
      if (request != null) {
        return request.getRequestURI();
      }
    }
    return "";
  }

  private HttpStatus status;

  public ResponseEntity<ApiResponse> toResponseEntity() {
    return new ResponseEntity<>(this, this.status);
  }

  public static ApiResponseBuilder<?, ?> builder(HttpStatus status) {
    return new ApiResponseBuilderImpl().status(status);
  }

  public abstract static class ApiResponseBuilder<
      C extends ApiResponse, B extends ApiResponseBuilder<C, B>> {

    public ApiResponseBuilder<C, B> request(HttpServletRequest request) {
      return self().path(request.getRequestURI());
    }
  }
}
