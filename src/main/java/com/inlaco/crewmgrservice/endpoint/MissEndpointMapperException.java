package com.inlaco.crewmgrservice.endpoint;

import org.springframework.web.method.HandlerMethod;

public class MissEndpointMapperException extends RuntimeException {

  HandlerMethod handlerMethod;

  public MissEndpointMapperException(HandlerMethod handlerMethod) {
    super(
        handlerMethod.getBeanType().getName()
            + "."
            + handlerMethod.getMethod().getName()
            + " must have @APIEndpointMap annotation");
    this.handlerMethod = handlerMethod;
  }
}
