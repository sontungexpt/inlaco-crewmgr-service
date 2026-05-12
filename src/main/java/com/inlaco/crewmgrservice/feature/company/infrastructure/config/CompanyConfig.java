package com.inlaco.crewmgrservice.feature.company.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(CompanyProperties.class)
public class CompanyConfig {
  // Configuration class for company management feature
}
