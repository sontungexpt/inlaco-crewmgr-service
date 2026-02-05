package com.inlaco.crewmgrservice.domain.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class ResourceDeleteFailedException extends BaseException {

  public ResourceDeleteFailedException(Class<?> resource, String field, Object value) {
    this(resource.getSimpleName() + " cannot be deleted", resource, field, value);
  }

  public ResourceDeleteFailedException(
      String message, Class<?> resource, String field, Object value) {
    this(message, resource, Map.of(field, value));
  }

  public ResourceDeleteFailedException(Class<?> resource, Map<String, Object> reasons) {
    this(resource.getSimpleName() + " cannot be deleted", resource, reasons);
  }

  public ResourceDeleteFailedException(
      String message, Class<?> resource, Map<String, Object> reasons) {

    super(
        "RESOURCE_DELETE_FAILED",
        message,
        HttpStatus.UNPROCESSABLE_ENTITY,
        Map.of("resource", resource.getSimpleName(), "reasons", reasons));
  }
}
