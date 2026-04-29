package com.inlaco.crewmgrservice.feature.notify.sender.pushnotification;

import com.inlaco.crewmgrservice.feature.notify.sender.NotificationSender;
import java.util.List;
import java.util.Map;
import lombok.Builder;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Service
public class ExpoNotificationSender implements NotificationSender<ExpoNotificationRequest, Object> {

  private static final String EXPO_PUSH_URL = "https://exp.host/--/api/v2/push/send";

  private final RestTemplate restTemplate = new RestTemplate();

  @Override
  public Class<ExpoNotificationRequest> getRequestType() {
    return ExpoNotificationRequest.class;
  }

  @Getter
  @Builder
  static class ExpoPushMessage {
    private String to;
    private String title;
    private String body;
    private Map<String, Object> data;
  }

  @Getter
  static class ExpoPushResponse {
    private List<ExpoPushTicket> data;
  }

  @Getter
  static class ExpoPushTicket {
    private String status;
    private String id;
    private String message;
  }

  @Override
  public Object sendNotification(ExpoNotificationRequest request) {

    log.info("Sending Expo notification request: {}", request);

    if (request.getRecipientTokens() == null || request.getRecipientTokens().isEmpty()) {
      log.warn("No recipients found");
      return null;
    }

    List<ExpoPushMessage> messages =
        request.getRecipientTokens().stream()
            .map(
                token -> {
                  log.debug("Preparing message for token: {}", token);

                  return ExpoPushMessage.builder()
                      .to(token)
                      .title(request.getTitle())
                      .body(request.getMessage())
                      .data(request.getData())
                      .build();
                })
            .toList();

    log.info("Built {} push messages", messages.size());

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<List<ExpoPushMessage>> entity = new HttpEntity<>(messages, headers);

    try {
      log.info("Sending request to Expo: {}", EXPO_PUSH_URL);

      var response = restTemplate.postForEntity(EXPO_PUSH_URL, entity, ExpoPushResponse.class);

      log.info("Expo response status: {}", response.getStatusCode());
      log.info("Expo response body: {}", response.getBody());

      return response;

    } catch (Exception ex) {
      log.error("Failed to send Expo notification", ex);
      throw ex;
    }
  }
}
