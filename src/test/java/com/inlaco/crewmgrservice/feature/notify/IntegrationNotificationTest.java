// package com.inlaco.crewmgrservice.feature.notify;

// import com.inlaco.crewmgrservice.feature.notify.httpsms.SMSRequest;
// import lombok.extern.slf4j.Slf4j;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.boot.test.context.SpringBootTest;
// import org.springframework.context.annotation.Profile;
// import org.springframework.test.context.junit.jupiter.SpringExtension;

// @SpringBootTest
// @ExtendWith(SpringExtension.class)
// @Slf4j
// @Profile("integration-test")
// public class IntegrationNotificationTest {

//   @Autowired private NotificationFactory notificationFactory;

//   @Test
//   public void testSendRealSMS() {
//     SMSRequest smsRequest = new SMSRequest("+84392211343", "This is a real test SMS!");
//     long startTime = System.nanoTime();
//     notificationFactory.sendNotification(NotificationType.SMS, smsRequest);
//     long endTime = System.nanoTime();
//     long durationInMilliseconds = (endTime - startTime) / 1_000_000;
//     log.debug("Time taken to send SMS: " + durationInMilliseconds + " ms");
//     assert durationInMilliseconds < 5000 : "SMS sending took too long!";
//   }

//   @Test
//   public void testSendRealSMSAsync() throws Exception {
//     SMSRequest smsRequest = new SMSRequest("+84392211343", "This is a real test SMS!");

//     notificationFactory
//         .sendNotificationAsync("SMS", smsRequest)
//         .thenAccept(
//             (result) -> {
//               log.debug("SMS sent successfully");
//             });

//     log.debug("Waiting for SMS to be sent...");
//   }
// }
