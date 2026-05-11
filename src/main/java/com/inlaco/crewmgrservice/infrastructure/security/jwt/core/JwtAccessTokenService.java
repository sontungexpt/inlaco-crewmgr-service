package com.inlaco.crewmgrservice.infrastructure.security.jwt.core;

import com.inlaco.crewmgrservice.feature.auth.application.port.in.AccessTokenGenerator;
import com.inlaco.crewmgrservice.infrastructure.security.auth.TokenSubjectExtractor;
import com.inlaco.crewmgrservice.infrastructure.security.jwt.config.JwtProperties;
import com.inlaco.crewmgrservice.shared.jwt.JwtClaimsMapper;
import com.inlaco.crewmgrservice.shared.jwt.JwtKeyProvider;
import com.inlaco.crewmgrservice.shared.jwt.JwtTokenGenerator;
import com.inlaco.crewmgrservice.shared.jwt.JwtTokenParser;
import io.jsonwebtoken.Claims;
import java.util.Collections;
import javax.crypto.SecretKey;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class JwtAccessTokenService implements AccessTokenGenerator, TokenSubjectExtractor {

  private final JwtTokenGenerator generator;
  private final JwtTokenParser parser;
  private final JwtClaimsMapper mapper;
  private final JwtProperties props;
  private final SecretKey secretKey;

  public JwtAccessTokenService(
      JwtTokenGenerator generator,
      JwtClaimsMapper mapper,
      JwtTokenParser parser,
      JwtProperties props) {
    this.generator = generator;
    this.mapper = mapper;
    this.parser = parser;
    this.props = props;
    this.secretKey = JwtKeyProvider.getSigningKey(props.getSecretKey());
  }

  @Override
  public String extractSubject(String token) {
    log.debug("Extracting subject from token: {}", token);
    Claims claims = parser.parse(token, secretKey);
    return mapper.extractSubject(claims);
  }

  @Override
  public String generate(String subject) {
    log.debug("Generating access token for subject: {}", subject);
    return generator.generate(subject, Collections.emptyMap(), props.getExpiration(), secretKey);
  }
}
