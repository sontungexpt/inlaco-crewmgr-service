package com.inlaco.crewmgrservice.application.exception;

import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceNotFoundException extends ApplicationException {

  private Map<String, Object> criteria;

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
    super("RESOURCE_NOT_FOUND", HttpStatus.NOT_FOUND, message);
    this.criteria = criteria;
  }
}
