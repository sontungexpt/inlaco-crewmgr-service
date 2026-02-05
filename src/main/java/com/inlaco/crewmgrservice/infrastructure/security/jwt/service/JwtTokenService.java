package com.inlaco.crewmgrservice.infrastructure.security.jwt.service;

import com.inlaco.crewmgrservice.feature.auth.model.RefreshToken;
import com.inlaco.crewmgrservice.feature.auth.repository.RefreshTokenRepository;
import com.inlaco.crewmgrservice.feature.auth.service.TokenService;
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
public class JwtTokenService implements TokenService {

  private final JwtTokenGenerator generator;
  private final JwtTokenParser parser;
  private final JwtClaimsMapper mapper;
  private final JwtProperties props;
  private final RefreshTokenRepository refreshTokenRepository;

  public String parseSubject(String token) {
    Claims claims = parser.parse(token);
    return mapper.extractSubject(claims);
  }

  @Override
  public String generateAccessToken(String subject) {
    return generator.generate(subject, Collections.emptyMap(), props.getAccessTokenExpiration());
  }

  @Override
  public RefreshToken generateRefreshToken(String subject) {
    RefreshToken refreshToken = new RefreshToken(subject, props.getRefreshTokenExpiration());
    return refreshTokenRepository.save(refreshToken);
  }
}
