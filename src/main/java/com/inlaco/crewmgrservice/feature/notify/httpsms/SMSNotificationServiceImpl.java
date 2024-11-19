package com.inlaco.crewmgrservice.feature.notify.httpsms;

import com.inlaco.crewmgrservice.feature.notify.NotificationService;
import com.inlaco.crewmgrservice.feature.notify.NotificationType;
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
@Service(NotificationType.SMS)
public class SMSNotificationServiceImpl implements NotificationService<SMSRequest> {

  @Value("${httpsms.api-key}")
  private String apiKey;

  @Value("${httpsms.sender}")
  private String sender;

  private String POST_URL = "https://api.httpsms.com/v1/messages/send";

  private String getRequestBody(SMSRequest request) {

    return String.format(
        """
        {
           "content": "%s",
           "from": "%s",
           "to": "%s"
        }
        """,
        request.getMessage(), sender, request.getRecipient());
  }

  @Override
  public void sendNotification(SMSRequest request) {
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
      log.info(client.send(httpRequest, HttpResponse.BodyHandlers.ofString()).body());
    } catch (IOException | InterruptedException e) {
      e.printStackTrace();
      log.warn("Error when sending sms message");
      throw new SMSNotificationException(request.getRecipient(), request.getMessage());
    }
  }
}
