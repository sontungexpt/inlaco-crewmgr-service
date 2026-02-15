package com.inlaco.crewmgrservice.feature.post.domain.exception;

import com.inlaco.crewmgrservice.shared.kernel.exception.ApplicationException;
import lombok.Getter;

@Getter
public class PostInactiveException extends ApplicationException {

  public PostInactiveException(String message) {
    super("POST_INACTIVE_ERROR", message);
  }
}
