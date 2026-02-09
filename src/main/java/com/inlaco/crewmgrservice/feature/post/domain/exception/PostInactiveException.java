package com.inlaco.crewmgrservice.feature.post.domain.exception;

import com.inlaco.crewmgrservice.application.exception.ApplicationException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostInactiveException extends ApplicationException {

  public PostInactiveException(String message) {
    super("POST_INACTIVE_ERROR", message, HttpStatus.BAD_REQUEST);
  }
}
