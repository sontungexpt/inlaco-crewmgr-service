package com.inlaco.crewmgrservice.application.exception;

import java.util.Map;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ResourceDeleteFailedException extends ApplicationException {

  private Map<String, Object> reasons;

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
    super("RESOURCE_DELETE_FAILED", HttpStatus.UNPROCESSABLE_ENTITY, message);
    this.reasons = reasons;
  }
}
