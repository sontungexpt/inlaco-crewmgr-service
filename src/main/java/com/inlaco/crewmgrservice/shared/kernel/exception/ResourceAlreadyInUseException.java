package com.inlaco.crewmgrservice.shared.kernel.exception;

import java.util.Map;
import lombok.Getter;

@Getter
public class ResourceAlreadyInUseException extends ApplicationException {

  private Map<String, Object> conflictFields;

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
    super("RESOURCE_ALREADY_IN_USE", message);
    this.conflictFields = conflictFields;
  }
}
