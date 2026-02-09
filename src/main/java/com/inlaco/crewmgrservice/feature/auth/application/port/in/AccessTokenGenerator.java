package com.inlaco.crewmgrservice.feature.auth.application.port.in;

public interface AccessTokenGenerator {

  String generate(String subject);
}
