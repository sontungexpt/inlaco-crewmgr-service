package com.inlaco.crewmgrservice.feature.notify.httpsms;

import com.inlaco.crewmgrservice.feature.notify.NotificationPolicy;
import com.inlaco.crewmgrservice.feature.notify.NotificationService;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class SMSNotificationServiceImpl implements NotificationService<SMSRequest> {

  @Value("${httpsms.api-key}")
  private String apiKey;

  @Value("${httpsms.sender}")
  private String sender;

  private String POST_URL = "https://api.httpsms.com/v1/messages/send";

  @Override
  public NotificationPolicy getPolicy() {
    return NotificationPolicy.SMS;
  }

  private String getRequestBody(SMSRequest request) {
    return String.format(
        """
        {
           "content": "%s",
           "from": "%s",
           "to": "%s"
        }
        """,
        request.getMessage(), sender, request.getFirstRecipient());
  }

  @Override
  public void sendNotification(SMSRequest request) {
    log.debug("Preparing to send SMS notification to recipient: {}", request.getFirstRecipient());
    HttpClient client = HttpClient.newHttpClient();
    HttpRequest httpRequest =
        HttpRequest.newBuilder()
            .uri(URI.create(POST_URL))
            .header("x-api-key", apiKey)
            .header("Content-Type", "application/json")
            .header("accept", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString(getRequestBody(request)))
            .build();

    try {
      log.info("Sending SMS notification to recipient: {}", request.getFirstRecipient());
      String response = client.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body();
      log.info("SMS notification sent successfully. Response: {}", response);
    } catch (IOException | InterruptedException e) {
      log.error(
          "Failed to send SMS notification to recipient: {}. Error: {}",
          request.getFirstRecipient(),
          e.getMessage());
      throw new SMSNotificationException(request.getFirstRecipient(), request.getMessage());
    }
  }
}
