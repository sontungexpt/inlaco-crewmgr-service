package com.inlaco.crewmgrservice.infrastructure.security.jwt.core;

import com.inlaco.crewmgrservice.feature.auth.application.port.in.AccessTokenGenerator;
import com.inlaco.crewmgrservice.infrastructure.security.auth.TokenSubjectExtractor;
import com.inlaco.crewmgrservice.infrastructure.security.jwt.config.JwtProperties;
import com.inlaco.crewmgrservice.infrastructure.security.jwt.crypto.JwtTokenGenerator;
import com.inlaco.crewmgrservice.infrastructure.security.jwt.crypto.JwtTokenParser;
import com.inlaco.crewmgrservice.infrastructure.security.jwt.mapper.JwtClaimsMapper;
import io.jsonwebtoken.Claims;
import java.util.Collections;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtAccessTokenService implements AccessTokenGenerator, TokenSubjectExtractor {

  private final JwtTokenGenerator generator;
  private final JwtTokenParser parser;
  private final JwtClaimsMapper mapper;
  private final JwtProperties props;

  @Override
  public String extractSubject(String token) {
    Claims claims = parser.parse(token);
    return mapper.extractSubject(claims);
  }

  @Override
  public String generate(String subject) {
    return generator.generate(subject, Collections.emptyMap(), props.getExpiration());
  }
}
