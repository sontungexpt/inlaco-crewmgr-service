package com.inlaco.crewmgrservice.feature.notify.sender.websocket;

import com.inlaco.crewmgrservice.feature.notify.sender.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationSender
    implements NotificationSender<WebSocketNotificationRequest, Object> {

  private final SimpMessagingTemplate messagingTemplate;

  @Override
  public Class<WebSocketNotificationRequest> getRequestType() {
    return WebSocketNotificationRequest.class;
  }

  @Override
  public Object sendNotification(WebSocketNotificationRequest request) {
    String channel = request.getDestination();
    Object payload = request.getPayload();

    for (String recipient : request.getRecipients()) {
      log.info(
          "[WS-SEND] Sending to user={} destination={} payload={}", recipient, channel, payload);
      messagingTemplate.convertAndSendToUser(recipient, channel, payload);
    }
    return null;
  }
}
