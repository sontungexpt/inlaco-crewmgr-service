package com.inlaco.crewmgrservice.infrastructure.websocket.interceptor;

import com.inlaco.crewmgrservice.feature.user.application.port.in.UserUseCase;
import com.inlaco.crewmgrservice.feature.user.domain.model.User;
import com.inlaco.crewmgrservice.infrastructure.security.auth.TokenSubjectExtractor;
import com.inlaco.crewmgrservice.infrastructure.websocket.core.WebSocketPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtChannelInterceptor implements ChannelInterceptor {

  private final UserUseCase userUseCase;
  private final TokenSubjectExtractor subjectExtractor;

  @Override
  public Message<?> preSend(Message<?> message, MessageChannel channel) {
    StompHeaderAccessor accessor =
        MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

    if (accessor != null && StompCommand.CONNECT.equals(accessor.getCommand())) {
      String token = accessor.getFirstNativeHeader("Authorization");

      if (token != null && token.startsWith("Bearer ")) {
        token = token.substring(7);
        String pubId = subjectExtractor.extractSubject(token);
        User user = userUseCase.findByPubId(pubId);
        WebSocketPrincipal webSocketUser = new WebSocketPrincipal(user.getId());
        accessor.setUser(webSocketUser);
      }
    }

    return message;
  }
}
