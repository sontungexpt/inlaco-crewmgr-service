package com.inlaco.crewmgrservice.feature.post.exception;

import com.inlaco.crewmgrservice.application.exception.ApplicationExceptionException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostInactiveException extends ApplicationExceptionException {

  public PostInactiveException(String message) {
    super("POST_INACTIVE_ERROR", message, HttpStatus.BAD_REQUEST);
  }
}
