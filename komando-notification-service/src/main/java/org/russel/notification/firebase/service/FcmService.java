package org.russel.notification.firebase.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.FirebaseMessagingException;
import com.google.firebase.messaging.Message;
import com.google.firebase.messaging.Notification;
import org.russel.komandocore.data.model.TaskNotification;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FcmService {
    @Autowired
    private final FirebaseMessaging firebaseMessaging;

    public FcmService(FirebaseMessaging firebaseMessaging) {
        this.firebaseMessaging = firebaseMessaging;
    }

    public void sendNotification(TaskNotification notification) {
        Message.Builder builder = Message.builder()
                .setNotification(Notification.builder()
                        .setTitle(notification.getTitle())
                        .setBody(notification.getBody())
                        .build())
                .setTopic(notification.getTopic())
                .putAllData(notification.getData());
        try {
            String response = firebaseMessaging.send(builder.build());
            System.out.println("FCM message sent to " + notification.getTopic() + ": " + response);
        } catch (FirebaseMessagingException e) {
            System.err.println("Error sending FCM message to " + notification.getTopic());
            e.printStackTrace();
        }
    }



    public void sendNotificationToTokens(TaskNotification notification) {
        if ((notification.getTopic() == null || notification.getTopic().isBlank()) &&
                (notification.getTokens() == null || notification.getTokens().isEmpty())) {
            System.out.println("No topic or tokens provided. Skipping notification.");
            return;
        }


        for (String token : notification.getTokens()) {
            Message message = Message.builder()
                    .setToken(token)
                    .setNotification(Notification.builder()
                            .setTitle(notification.getTitle())
                            .setBody(notification.getBody())
                            .build())
                    .putAllData(notification.getData())
                    .build();
            try {
                String response = firebaseMessaging.send(message);
                System.out.println("FCM message sent to token " + token + ": " + response);
            } catch (FirebaseMessagingException e) {
                System.err.println("Error sending FCM message to token: " + token);
                e.printStackTrace();
            }
        }
    }


    public void sendTestNotification(String token) {
        Message message = Message.builder()
                .setToken(token)
                .setNotification(Notification.builder()
                        .setTitle("Test Notification")
                        .setBody("This is a test message from Spring Boot 🚀")
                        .build())
                .putData("type", "test")
                .build();

        try {
            String response = firebaseMessaging.send(message);
            System.out.println("Test message sent: " + response);
        } catch (FirebaseMessagingException e) {
            e.printStackTrace();
        }
    }


}
