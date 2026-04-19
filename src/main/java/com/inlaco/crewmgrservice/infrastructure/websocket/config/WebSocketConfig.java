package com.inlaco.crewmgrservice.infrastructure.websocket.config;

import com.inlaco.crewmgrservice.infrastructure.websocket.interceptor.JwtChannelInterceptor;
import com.inlaco.crewmgrservice.shared.constant.WebSocketContants;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
@Slf4j
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private final JwtChannelInterceptor jwtChannelInterceptor;

  private static final String[] ALLOWED_ORIGINS = {
    "http://localhost:*", // Enable localhost
    "http://192.168.*:*", // Enable local IP
    "https://sontungexpt.github.io",
    "https://inlaco-crewmgr-service-b7btdkgsdwafb2ht.eastasia-01.azurewebsites.net"
  };

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    registry
        .addEndpoint(WebSocketContants.ENDPOINT)
        .setAllowedOriginPatterns(ALLOWED_ORIGINS)
        .withSockJS();
  }

  @Override
  public void configureMessageBroker(MessageBrokerRegistry registry) {
    registry.enableSimpleBroker("/topic", "/queue");
    registry.setUserDestinationPrefix("/user");
    registry.setApplicationDestinationPrefixes("/app");
  }

  @Override
  public void configureClientInboundChannel(ChannelRegistration registration) {
    registration.interceptors(jwtChannelInterceptor);
  }
}
