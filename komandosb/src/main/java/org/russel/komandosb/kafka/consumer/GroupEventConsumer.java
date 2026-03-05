package org.russel.komandosb.kafka.consumer;

import org.russel.komandosb.data.model.TaskNotification;
import org.russel.komandosb.data.model.event.GroupDeletedEvent;
import org.russel.komandosb.data.model.event.GroupUpdatedEvent;
import org.russel.komandosb.data.model.event.GroupUserAssignedEvent;
import org.russel.komandosb.data.model.event.GroupUserRemovedEvent;
import org.russel.komandosb.firebase.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@KafkaListener(topics = "group-events", groupId = "notification-group")
public class GroupEventConsumer {

    @Autowired
    private FcmService fcmService;

    @KafkaHandler
    public void handleGroupUpdated(GroupUpdatedEvent event) {
        System.out.println("===========================================");
        System.out.println("GROUP UPDATED!:");
        System.out.println("    Group ID: " + event.getGroupId());
        System.out.println("    Group Name: " + event.getGroupName());
        System.out.println("    Updated By: " + event.getUpdaterName());
        System.out.println("    Updated At: " + event.getTimestamp());
        System.out.println("===========================================");

        String title = "GROUP DETAILS UPDATED";
        String body = event.getUpdaterName() + " updated the group \"" + event.getGroupName()+ "\".";

        Map<String, String> data = new HashMap<>();
        data.put("groupId", event.getGroupId().toString());
        data.put("eventType", event.getEventType().name());


        TaskNotification notification = new TaskNotification(
                title,
                body,
                "group-" + event.getGroupId(),
                null,
                data
        );
        fcmService.sendNotification(notification);
    }

    @KafkaHandler
    public void handleGroupDeleted(GroupDeletedEvent event) {
        System.out.println("===========================================");
        System.out.println("GROUP DELETED!:");
        System.out.println("    Group ID: " + event.getGroupId());
        System.out.println("    Group Name: " + event.getGroupName());
        System.out.println("    Deleted By: " + event.getDeleterName());
        System.out.println("    Deleted At: " + event.getTimestamp());
        System.out.println("===========================================");

        String title = "GROUP DELETED!";
        String body = "Group \"" + event.getGroupName() + "\" has been deleted by " + event.getDeleterName();

        Map<String, String> data = new HashMap<>();
        data.put("groupId", event.getGroupId().toString());
        data.put("eventType", event.getEventType().name());

        TaskNotification notification = new TaskNotification(
                title,
                body,
                "group-" + event.getGroupId(),
                null,
                data
        );
        fcmService.sendNotification(notification);

    }

    @KafkaHandler
    public void handleGroupUserAssigned(GroupUserAssignedEvent event) {
        System.out.println("===========================================");
        System.out.println("USERS ASSIGNED TO GROUP!:");
        System.out.println("    Group ID: " + event.getGroupId());
        System.out.println("    Group Name: " + event.getGroupName());
        System.out.println("    Assigned Users: " + event.getAssignedUserIds());
        System.out.println("    Assigned By: " + event.getAssignerName());
        System.out.println("    Assigned At: " + event.getTimestamp());
        System.out.println("===========================================");

        List<String> tokens = event.getAssignedUserTokens();

        if (tokens.isEmpty()) {
            System.out.println("No FCM tokens available for assigned users. Skipping notification.");
            return;
        }

        String title = "NEW GROUP";
        String body = event.getAssignerName() + " added you to \"" + event.getGroupName() + "\"";

        Map<String, String> data = new HashMap<>();
        data.put("taskId", event.getGroupId().toString());
        data.put("eventType", event.getEventType().name());

        TaskNotification notification = new TaskNotification(
                title,
                body,
                null,
                tokens,
                data
        );

        fcmService.sendNotificationToTokens(notification);
    }

    @KafkaHandler
    public void handleGroupUserRemoved(GroupUserRemovedEvent event) {
        System.out.println("===========================================");
        System.out.println("USERS REMOVED FROM GROUP!:");
        System.out.println("    Group ID: " + event.getGroupId());
        System.out.println("    Group Name: " + event.getGroupName());
        System.out.println("    Removed Users: " + event.getRemovedUserIds());
        System.out.println("    Removed By: " + event.getRemoverName());
        System.out.println("    Removed At: " + event.getTimestamp());
        System.out.println("===========================================");

        List<String> tokens = event.getRemovedUserTokens();

        if (tokens.isEmpty()) {
            System.out.println("No FCM tokens available for removed users. Skipping notification.");
            return;
        }

        String title = "REMOVED FROM GROUP.";
        String body = event.getRemoverName() + " removed you from \"" + event.getGroupName() + "\"";

        Map<String, String> data = new HashMap<>();
        data.put("taskId", event.getGroupId().toString());
        data.put("eventType", event.getEventType().name());

        TaskNotification notification = new TaskNotification(
                title,
                body,
                null,
                tokens,
                data
        );

        fcmService.sendNotificationToTokens(notification);
    }

}
