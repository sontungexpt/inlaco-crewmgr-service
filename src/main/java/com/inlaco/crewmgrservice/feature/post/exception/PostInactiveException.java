package com.inlaco.crewmgrservice.feature.post.exception;

import com.inlaco.crewmgrservice.exceptions.BaseException;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class PostInactiveException extends BaseException {

  public PostInactiveException(String message) {
    super(HttpStatus.FORBIDDEN, message);
  }
}
