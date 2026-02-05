package com.inlaco.crewmgrservice.infrastructure.security.jwt.crypto;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenGenerator {

  private final JwtKeyProvider keyProvider;

  public JwtTokenGenerator(JwtKeyProvider keyProvider) {
    this.keyProvider = keyProvider;
  }

  public String generate(String subject, Map<String, Object> claims, long ttlMs) {
    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + ttlMs))
        .signWith(keyProvider.getSigningKey())
        .compact();
  }
}
