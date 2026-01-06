package com.inlaco.crewmgrservice.feature.notify;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.inlaco.crewmgrservice.feature.notify.httpsms.SMSNotificationServiceImpl;
import com.inlaco.crewmgrservice.feature.notify.httpsms.SMSRequest;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailNotificationServiceImpl;
import com.inlaco.crewmgrservice.feature.notify.mail.EmailRequest;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class NotificationTest {

  @Mock private SMSNotificationServiceImpl smsNotificationService;
  @Mock private EmailNotificationServiceImpl emailNotificationService;

  @Mock private Map<String, NotificationService> notificationServiceMap;
  // private Map<String, NotificationService> notificationServiceMap;

  private NotificationFactory notificationFactory;

  @BeforeEach
  void setup() {
    when(notificationServiceMap.get(NotificationType.SMS)).thenReturn(smsNotificationService);
    when(notificationServiceMap.get(NotificationType.EMAIL)).thenReturn(emailNotificationService);

    // Inject map mock vào NotificationFactory
    notificationFactory = new NotificationFactory(notificationServiceMap);
  }

  @Test
  public void testSendSMS() {
    SMSRequest smsRequest = new SMSRequest("+84392211343", "Test sms");
    notificationFactory.sendNotification(NotificationType.SMS, smsRequest);
    verify(smsNotificationService).sendNotification(smsRequest);

    EmailRequest notificationRequest = new EmailRequest("sontungexpt@gmail.com", "test", "test");
    notificationFactory.sendNotification(NotificationType.EMAIL, notificationRequest);
    verify(emailNotificationService).sendNotification(notificationRequest);
  }
}
