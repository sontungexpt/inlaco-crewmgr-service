package com.inlaco.crewmgrservice.infrastructure.security.jwt.core;

import com.inlaco.crewmgrservice.feature.auth.infrastructure.security.AuthorityResolver;
import com.inlaco.crewmgrservice.feature.auth.infrastructure.security.SecurityUser;
import com.inlaco.crewmgrservice.feature.user.application.port.in.UserUseCase;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.security.auth.TokenAuthenticator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class JwtTokenAuthenticator implements TokenAuthenticator {

  private final JwtAccessTokenService jwtTokenService;
  private final UserUseCase userUseCase;
  private final AuthorityResolver authorityResolver;

  @Override
  public Authentication authenticate(String token, Object details) {

    try {
      log.debug("[JWT-AUTH] Starting authentication");

      String pubId = jwtTokenService.extractSubject(token);
      log.debug("[JWT-AUTH] Parsed subject pubId={}", pubId);

      User user = userUseCase.findByPubId(pubId);
      log.debug("[JWT-AUTH] User found id={}, pubId={}", user.getId(), pubId);

      SecurityUser su = new SecurityUser(user, authorityResolver.resolve(user));

      log.debug("[JWT-AUTH] Authorities resolved for userId={}", user.getId());

      UsernamePasswordAuthenticationToken auth =
          new UsernamePasswordAuthenticationToken(su, null, su.getAuthorities());

      auth.setDetails(details);

      log.info("[JWT-AUTH] Authentication successful for pubId={}", pubId);

      return auth;

    } catch (Exception ex) {
      log.error("[JWT-AUTH] Authentication failed. reason={}", ex.getMessage(), ex);
      throw ex;
    }
  }
}
