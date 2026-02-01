package com.inlaco.crewmgrservice.exceptions;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

@Getter
@Slf4j
public class ResourceNotFoundException extends BaseException {

  private String resourceName;

  private Map<String, Object> conflictFields = new HashMap<>();

  public <T> ResourceNotFoundException(
      Class<T> resource, String conflictFieldName, Object conflictFieldValue) {
    this(
        String.format(
            "%s not found with %s : '%s'", resource, conflictFieldName, conflictFieldValue),
        resource,
        conflictFieldName,
        conflictFieldValue);
  }

  public <T> ResourceNotFoundException(
      String message,
      @NonNull Class<T> resource,
      @NonNull String conflictFieldName,
      Object conflictFieldValue) {
    super(HttpStatus.NOT_FOUND, message);
    this.resourceName = resource.getSimpleName();
    this.conflictFields.put(conflictFieldName, conflictFieldValue);
  }

  public <T> ResourceNotFoundException(Class<T> resource, Map<String, Object> conflictFields) {
    this(
        String.format("%s not found with %s", resource, conflictFields.toString()),
        resource,
        conflictFields);
  }

  public <T> ResourceNotFoundException(
      String message, @NonNull Class<T> resource, Map<String, Object> conflictFields) {
    super(HttpStatus.NOT_FOUND, message);
    this.resourceName = resource.getSimpleName();
    this.conflictFields = conflictFields;
  }

  @Override
  public Object getData() {
    return new Object() {
      public Map<String, Object> getConflictFields() {
        return conflictFields;
      }
    };
  }
}
