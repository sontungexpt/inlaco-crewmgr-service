package com.inlaco.crewmgrservice.feature.notify.application.port.service;

import com.inlaco.crewmgrservice.feature.notify.domain.model.WebSocketNotificationPayload;
import com.inlaco.crewmgrservice.feature.notify.domain.model.WebSocketNotificationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationService
    implements NotificationService<WebSocketNotificationRequest, Object> {

  private final SimpMessagingTemplate messagingTemplate;

  @Override
  public Class<WebSocketNotificationRequest> getRequestType() {
    return WebSocketNotificationRequest.class;
  }

  @Override
  public Object sendNotification(WebSocketNotificationRequest request) {
    String channel = request.getDestination();
    WebSocketNotificationPayload payload = request.getPayload();

    for (String recipient : request.getRecipients()) {
      log.info(
          "[WS-SEND] Sending to user={} destination={} payload={}", recipient, channel, payload);
      messagingTemplate.convertAndSendToUser(recipient, channel, payload);
    }
    return null;
  }
}
