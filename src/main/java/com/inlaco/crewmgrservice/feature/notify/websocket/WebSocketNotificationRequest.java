package com.inlaco.crewmgrservice.feature.notify.websocket;

import com.inlaco.crewmgrservice.feature.notify.NotificationRequest;
import java.util.List;
import lombok.Getter;

@Getter
public class WebSocketNotificationRequest extends NotificationRequest<String, String> {

  private WebSocketNotificationPayload payload;
  private String channel;

  public WebSocketNotificationRequest(
      String sender,
      List<String> recipients,
      String channel,
      WebSocketNotificationPayload payload) {
    super(sender, recipients, payload.getMessage());
    this.payload = payload;
    this.channel = channel;
  }
}
