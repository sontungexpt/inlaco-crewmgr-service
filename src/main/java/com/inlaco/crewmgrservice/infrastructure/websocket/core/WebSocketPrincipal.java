package com.inlaco.crewmgrservice.infrastructure.websocket.core;

import java.security.Principal;

public class WebSocketPrincipal implements Principal {

  private String id;

  public WebSocketPrincipal(String id) {
    this.id = id;
  }

  @Override
  public String getName() {
    return id;
  }
}
