package com.inlaco.crewmgrservice.infrastructure.security.jwt.crypto;

import com.inlaco.crewmgrservice.infrastructure.security.jwt.exception.JwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class JwtTokenParser {

  private final JwtKeyProvider keyProvider;

  public JwtTokenParser(JwtKeyProvider keyProvider) {
    this.keyProvider = keyProvider;
  }

  public Claims parse(String token) {
    try {
      return Jwts.parser()
          .verifyWith(keyProvider.getSigningKey())
          .build()
          .parseSignedClaims(token)
          .getPayload();
    } catch (MalformedJwtException ex) {
      log.info("Malformed JWT token");
      throw new JwtTokenException(token, "Malformed jwt token");

    } catch (ExpiredJwtException ex) {
      log.info("JWT token expired");
      throw new JwtTokenException(token, "Token expired. Refresh required");

    } catch (UnsupportedJwtException ex) {
      log.info("Unsupported JWT token");
      throw new JwtTokenException(token, "Unsupported JWT token");

    } catch (IllegalArgumentException ex) {
      log.info("Illegal argument token");
      throw new JwtTokenException(token, "Illegal argument token");

    } catch (JwtException ex) {
      log.info("Invalid JWT token");
      throw new JwtTokenException(token, "Invalid JWT token");
    }
  }
}
