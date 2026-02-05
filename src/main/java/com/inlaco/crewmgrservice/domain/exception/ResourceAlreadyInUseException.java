package com.inlaco.crewmgrservice.domain.exception;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class ResourceAlreadyInUseException extends BaseException {

  public ResourceAlreadyInUseException(Class<?> resource, String field, Object value) {
    this(resource.getSimpleName() + " already in use", resource, field, value);
  }

  public ResourceAlreadyInUseException(
      String message, Class<?> resource, String field, Object value) {
    this(message, resource, Map.of(field, value));
  }

  public ResourceAlreadyInUseException(Class<?> resource, Map<String, Object> conflictFields) {
    this(resource.getSimpleName() + " already in use", resource, conflictFields);
  }

  public ResourceAlreadyInUseException(
      String message, Class<?> resource, Map<String, Object> conflictFields) {
    super(
        "RESOURCE_ALREADY_IN_USE",
        message,
        HttpStatus.CONFLICT,
        Map.of("resource", resource.getSimpleName(), "conflictFields", conflictFields));
  }

  public Object getConflictFields() {
    return super.getData();
  }
}
