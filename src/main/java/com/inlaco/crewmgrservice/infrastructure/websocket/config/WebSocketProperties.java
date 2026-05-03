package com.inlaco.crewmgrservice.infrastructure.websocket.config;

import jakarta.annotation.PostConstruct;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@Setter
@Slf4j
@ConfigurationProperties(prefix = "websocket")
public class WebSocketProperties {

  private String endpoint;
  private List<String> allowedOrigins;

  @PostConstruct
  public void debug() {
    log.info("WebSocket endpoint: {}", endpoint);
    log.info("WebSocket allowed origins: {}", allowedOrigins);
  }
}
