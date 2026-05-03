package com.inlaco.crewmgrservice.infrastructure.websocket.config;

import com.inlaco.crewmgrservice.infrastructure.websocket.interceptor.JwtChannelInterceptor;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
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
@EnableConfigurationProperties(WebSocketProperties.class)
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
  private final JwtChannelInterceptor jwtChannelInterceptor;
  private final WebSocketProperties webSocketProperties;

  @Override
  public void registerStompEndpoints(StompEndpointRegistry registry) {
    List<String> origins = webSocketProperties.getAllowedOrigins();

    registry
        .addEndpoint(webSocketProperties.getEndpoint())
        .setAllowedOriginPatterns(
            origins != null && !origins.isEmpty()
                ? origins.toArray(new String[0])
                : new String[] {"*"})
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
