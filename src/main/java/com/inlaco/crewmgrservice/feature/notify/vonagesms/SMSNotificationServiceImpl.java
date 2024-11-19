// package com.inlaco.crewmgrservice.feature.notify.vonagesms;

// import com.inlaco.crewmgrservice.feature.notify.NotificationService;
// import com.inlaco.crewmgrservice.feature.notify.NotificationType;
// import com.vonage.client.VonageClient;
// import com.vonage.client.sms.MessageStatus;
// import com.vonage.client.sms.SmsSubmissionResponse;
// import com.vonage.client.sms.messages.TextMessage;
// import lombok.RequiredArgsConstructor;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.stereotype.Service;

// @Slf4j
// @RequiredArgsConstructor
// @Service(NotificationType.SMS)
// public class SMSNotificationServiceImpl implements NotificationService<SMSRequest> {

//   @Value("${vonage.api-key}")
//   private String API_KEY;

//   @Value("${vonage.api-secret}")
//   private String API_SECRET;

//   @Value("${vonage.sender}")
//   private String SENDER;

//   @Override
//   public void sendNotification(SMSRequest request) {
//     VonageClient client = VonageClient.builder().apiKey(API_KEY).apiSecret(API_SECRET).build();
//     // String sender = request.getSender() != null ? request.getSender() : SENDER;
//     TextMessage message =
//         new TextMessage("+84392211343", request.getRecipient(), request.getMessage());
//     SmsSubmissionResponse response = client.getSmsClient().submitMessage(message);

//     if (response.getMessages().get(0).getStatus() == MessageStatus.OK) {
//       log.info("Message sent successfully.");
//     } else {
//       log.warn("Message failed with error: " + response.getMessages().get(0).getErrorText());
//     }
//   }
// }
