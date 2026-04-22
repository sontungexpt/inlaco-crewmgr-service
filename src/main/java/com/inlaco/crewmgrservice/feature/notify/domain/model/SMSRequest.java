package com.inlaco.crewmgrservice.feature.notify.domain.model;

import lombok.Getter;

@Getter
public class SMSRequest implements NotificationRequest {

  private String recipient;
  private String message;

  public SMSRequest(String sender, String recipient, String message) {
    this.recipient = recipient;
    this.message = message;
  }
}
