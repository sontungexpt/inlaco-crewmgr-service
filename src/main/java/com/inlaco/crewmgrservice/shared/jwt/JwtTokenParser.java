package com.inlaco.crewmgrservice.shared.jwt;

import com.inlaco.crewmgrservice.infrastructure.security.jwt.exception.JwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenParser {

  public Claims parse(String token, SecretKey secretKey) {
    try {
      return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).getPayload();
    } catch (MalformedJwtException ex) {
      log.debug("[JWT] Malformed JWT token");
      throw new JwtTokenException(token, "Malformed jwt token");

    } catch (ExpiredJwtException ex) {
      log.debug("[JWT] JWT token expired");
      throw new JwtTokenException(token, "Token expired. Refresh required");

    } catch (UnsupportedJwtException ex) {
      log.debug("Unsupported JWT token");
      throw new JwtTokenException(token, "Unsupported JWT token");

    } catch (IllegalArgumentException ex) {
      log.debug("Illegal argument token");
      throw new JwtTokenException(token, "Illegal argument token");

    } catch (JwtException ex) {
      log.debug("Invalid JWT token");
      throw new JwtTokenException(token, "Invalid JWT token");
    }
  }
}
