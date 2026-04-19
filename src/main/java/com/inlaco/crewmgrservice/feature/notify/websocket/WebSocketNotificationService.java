package com.inlaco.crewmgrservice.feature.notify.websocket;

import com.inlaco.crewmgrservice.feature.notify.NotificationPolicy;
import com.inlaco.crewmgrservice.feature.notify.NotificationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class WebSocketNotificationService
    implements NotificationService<WebSocketNotificationRequest> {

  private final SimpMessagingTemplate messagingTemplate;

  @Override
  public NotificationPolicy getPolicy() {
    return NotificationPolicy.WEB_SOCKET;
  }

  @Override
  public void sendNotification(WebSocketNotificationRequest request) {
    String channel = request.getChannel();
    WebSocketNotificationPayload payload = request.getPayload();

    for (String recipient : request.getRecipients()) {
      messagingTemplate.convertAndSendToUser(recipient, channel, payload);
      log.info("[WS-NOTIFY] Sent to pubId={}", recipient);
    }
  }
}
