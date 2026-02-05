package com.inlaco.crewmgrservice.infrastructure.security.jwt.mapper;

import io.jsonwebtoken.Claims;
import org.springframework.stereotype.Component;

@Component
public class JwtClaimsMapper {

  public String extractSubject(Claims claims) {
    return claims.getSubject();
  }
}
