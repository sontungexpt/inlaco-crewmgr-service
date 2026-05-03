package com.inlaco.crewmgrservice.infrastructure.security.auth;

public interface TokenSubjectExtractor {

  String extractSubject(String token);
}
