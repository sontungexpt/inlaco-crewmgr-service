package com.inlaco.crewmgrservice.infrastructure.config;

import com.resend.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ResendConfig {

  @Value("${resend.api-key}")
  private String RESEND_API_KEY;

  @Bean
  public Resend resend() {
    return new Resend(RESEND_API_KEY);
  }
}
