package com.inlaco.crewmgrservice.feature.post.exception;

import com.inlaco.crewmgrservice.domain.exception.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostInactiveException extends BaseException {

  public PostInactiveException(String message) {
    super("POST_INACTIVE_ERROR", message, HttpStatus.BAD_REQUEST);
  }
}
