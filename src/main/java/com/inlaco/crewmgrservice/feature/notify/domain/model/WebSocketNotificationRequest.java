package com.inlaco.crewmgrservice.feature.notify.domain.model;

import java.util.List;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

@Getter
@SuperBuilder
public class WebSocketNotificationRequest implements NotificationRequest {

  private List<String> recipients;
  private WebSocketNotificationPayload payload;
  private String destination;

  public WebSocketNotificationRequest(
      List<String> recipients, String destination, WebSocketNotificationPayload payload) {
    this.payload = payload;
    this.destination = destination;
    this.recipients = recipients;
  }
}
