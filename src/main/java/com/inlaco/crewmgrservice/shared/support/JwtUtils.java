package com.inlaco.crewmgrservice.shared.support;

import com.inlaco.crewmgrservice.infrastructure.security.jwt.exception.JwtTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.Map;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtils {

  public static SecretKey getSigningKey(String secret) {
    byte[] keyBytes = Decoders.BASE64.decode(secret);
    return Keys.hmacShaKeyFor(keyBytes);
  }

  public static String generate(
      SecretKey signingKey, String subject, Map<String, Object> claims, long ttlMs) {
    return Jwts.builder()
        .claims(claims)
        .subject(subject)
        .issuedAt(new Date())
        .expiration(new Date(System.currentTimeMillis() + ttlMs))
        .signWith(signingKey)
        .compact();
  }

  public static Claims parse(String token, SecretKey signingKey) {
    try {
      return Jwts.parser().verifyWith(signingKey).build().parseSignedClaims(token).getPayload();
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

  public static String extractSubject(String token, SecretKey signingKey) {
    Claims claims = parse(token, signingKey);
    return claims.getSubject();
  }
}
