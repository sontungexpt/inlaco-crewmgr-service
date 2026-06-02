package com.inlaco.crewmgrservice.shared.jwt;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class JwtClaimsMapper {

  public String extractSubject(Claims claims) {
    return claims.getSubject();
  }
}
