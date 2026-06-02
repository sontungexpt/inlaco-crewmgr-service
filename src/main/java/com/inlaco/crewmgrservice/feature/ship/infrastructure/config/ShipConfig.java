package com.inlaco.crewmgrservice.feature.ship.infrastructure.config;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties(ShipProperties.class)
public class ShipConfig {
  // Configuration class for ship management feature
}
