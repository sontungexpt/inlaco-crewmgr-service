package com.inlaco.crewmgrservice.exceptions;

import java.util.HashMap;
import java.util.Map;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;

@Getter
@Slf4j
public class ResourceDeleteFailedException extends BaseException {

  private String resource;
  private Map<String, Object> conflictFields = new HashMap<>();

  public <T> ResourceDeleteFailedException(
      Class<T> resource, String conflictFieldName, Object conflictFieldValue) {
    this(
        String.format(
            "%s already in use with %s : '%s'", resource, conflictFieldName, conflictFieldValue),
        resource,
        conflictFieldName,
        conflictFieldValue);
  }

  public <T> ResourceDeleteFailedException(
      String message,
      @NonNull Class<T> resource,
      @NonNull String conflictFieldName,
      Object conflictFieldValue) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, message);
    try {
      resource.getDeclaredField(conflictFieldName);

      this.resource = resource.getSimpleName();
      this.conflictFields.put(conflictFieldName, conflictFieldValue);
    } catch (NoSuchFieldException e) {
      log.error("Field {} not found in class {}", conflictFieldName, resource.getSimpleName());
      throw new RuntimeException(
          String.format(
              "Field %s not found in class %s", conflictFieldName, resource.getSimpleName()));
    }
  }

  public <T> ResourceDeleteFailedException(Class<T> resource, Map<String, Object> conflictFields) {
    this(
        String.format("%s already in use with %s", resource, conflictFields.toString()),
        resource,
        conflictFields);
  }

  public <T> ResourceDeleteFailedException(
      String message, @NonNull Class<T> resource, Map<String, Object> conflictFields) {
    super(HttpStatus.UNPROCESSABLE_ENTITY, message);
    try {

      for (String conflictField : conflictFields.keySet()) {
        resource.getDeclaredField(conflictField);
      }

      this.resource = resource.getSimpleName();
      this.conflictFields = conflictFields;

    } catch (NoSuchFieldException e) {
      log.error(
          "Field {} not found in class {}", conflictFields.toString(), resource.getSimpleName());
      throw new RuntimeException(
          String.format(
              "Field %s not found in class %s",
              conflictFields.toString(), resource.getSimpleName()));
    }
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
