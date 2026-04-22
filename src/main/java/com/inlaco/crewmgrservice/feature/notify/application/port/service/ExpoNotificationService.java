package com.inlaco.crewmgrservice.feature.notify.application.port.service;

import com.inlaco.crewmgrservice.feature.notify.domain.model.ExpoNotificationRequest;
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
public class ExpoNotificationService
    implements NotificationService<ExpoNotificationRequest, Object> {

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

  @Override
  public Object sendNotification(ExpoNotificationRequest request) {

    if (request.getRecipients() == null || request.getRecipients().isEmpty()) {
      log.warn("No recipients");
      return null;
    }

    List<ExpoPushMessage> messages =
        request.getRecipients().stream()
            .map(
                token ->
                    ExpoPushMessage.builder()
                        .to(token)
                        .title(request.getTitle())
                        .body(request.getMessage())
                        .data(request.getData())
                        .build())
            .toList();

    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);

    HttpEntity<List<ExpoPushMessage>> entity = new HttpEntity<>(messages, headers);

    return restTemplate.postForEntity(EXPO_PUSH_URL, entity, String.class);
  }
}
