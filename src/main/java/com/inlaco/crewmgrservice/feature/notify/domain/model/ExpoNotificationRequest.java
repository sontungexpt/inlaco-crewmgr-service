package com.inlaco.crewmgrservice.feature.notify.domain.model;

import java.util.List;
import java.util.Map;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
public class ExpoNotificationRequest implements NotificationRequest {

  private List<String> recipients;

  private String title;
  private String message;

  private Map<String, Object> data;

  public ExpoNotificationRequest(
      List<String> recipients, String title, String message, Map<String, Object> data) {

    this.recipients = recipients;
    this.title = title;
    this.message = message;
    this.data = data;
  }
}
