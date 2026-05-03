package com.inlaco.crewmgrservice.feature.notify.sender.websocket;

import com.inlaco.crewmgrservice.feature.notify.sender.NotificationRequest;
import java.util.Collections;
import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class WebSocketNotificationRequest implements NotificationRequest {

  private List<String> recipients;
  private Object payload;
  private String destination;

  public WebSocketNotificationRequest(String recipient, String destination, Object payload) {
    this.payload = payload;
    this.destination = destination;
    this.recipients = Collections.singletonList(recipient);
  }

  public WebSocketNotificationRequest(List<String> recipients, String destination, Object payload) {
    this.payload = payload;
    this.destination = destination;
    this.recipients = recipients;
  }
}
