package com.inlaco.crewmgrservice.domain.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class ResourceNotFoundException extends BaseException {

  public ResourceNotFoundException(Class<?> resource, String field, Object value) {
    this(resource.getSimpleName() + " not found", resource, field, value);
  }

  public ResourceNotFoundException(String message, Class<?> resource, String field, Object value) {
    this(message, resource, Map.of(field, value));
  }

  public ResourceNotFoundException(Class<?> resource, Map<String, Object> criteria) {
    this(resource.getSimpleName() + " not found", resource, criteria);
  }

  public ResourceNotFoundException(
      String message, Class<?> resource, Map<String, Object> criteria) {
    super(
        "RESOURCE_NOT_FOUND",
        message,
        HttpStatus.NOT_FOUND,
        Map.of("resource", resource.getSimpleName(), "criteria", criteria));
  }
}
