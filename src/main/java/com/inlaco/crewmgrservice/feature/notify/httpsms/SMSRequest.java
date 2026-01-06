package com.inlaco.crewmgrservice.feature.notify.httpsms;

import com.inlaco.crewmgrservice.feature.notify.NotificationRequest;

public class SMSRequest extends NotificationRequest<String, String> {

  public SMSRequest(String recipient, String message) {
    super(null, recipient, message);
  }

  public SMSRequest(String sender, String recipient, String message) {
    super(sender, recipient, message);
  }
}
