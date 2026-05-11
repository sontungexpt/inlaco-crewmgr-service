package com.inlaco.crewmgrservice.shared.jwt;

import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenGenerator {

  public String generate(
      String subject, Map<String, Object> claims, long ttlMs, SecretKey secretKey) {
    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + ttlMs))
        .signWith(secretKey)
        .compact();
  }
}
