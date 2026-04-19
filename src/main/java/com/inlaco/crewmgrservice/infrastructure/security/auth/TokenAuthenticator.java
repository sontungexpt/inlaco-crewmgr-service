package com.inlaco.crewmgrservice.infrastructure.security.auth;

import org.springframework.security.core.Authentication;

public interface TokenAuthenticator {
  Authentication authenticate(String token, Object details);

  default Authentication authenticate(String token) {
    return authenticate(token, null);
  }
}
