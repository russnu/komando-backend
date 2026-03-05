package org.russel.komandosb.kafka.consumer;

import org.russel.komandosb.data.model.TaskNotification;
import org.russel.komandosb.data.model.event.*;
import org.russel.komandosb.firebase.FcmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@KafkaListener(topics = "task-events", groupId = "notification-group")
public class TaskEventConsumer {

    @Autowired
    private FcmService fcmService;

    @KafkaHandler
    public void handleTaskCreated(TaskCreatedEvent event) {
        System.out.println("===========================================");
        System.out.println("TASK CREATED!:");
        System.out.println("    Task ID: " + event.getTaskId());
        System.out.println("    Title: " + event.getTitle());
        System.out.println("    Description: " + event.getDescription());
        System.out.println("    Group: " + event.getGroupName());
        System.out.println("    Status: " + event.getStatus());
        System.out.println("    Created By: " + event.getCreatorName());
        System.out.println("    Created At: " + event.getTimestamp());
        System.out.println("===========================================");

        String title = "NEW TASK: \"" + event.getTitle() + "\"";
        String body = event.getDescription() != null ? event.getDescription() : "A new task has been added!";

        Map<String, String> data = new HashMap<>();
        data.put("taskId", event.getTaskId().toString());
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
    public void handleTaskUpdated(TaskUpdatedEvent event) {
        System.out.println("===========================================");
        System.out.println("TASK UPDATED!:");
        System.out.println("    Task ID: " + event.getTaskId());
        System.out.println("    Updated By: " + event.getUpdateName());
        System.out.println("    Group: " + event.getGroupName());
        System.out.println("    Title: " + event.getTitle());
        System.out.println("    Description: " + event.getDescription());
        System.out.println("    Status: " + event.getStatus());
        System.out.println("    Updated At: " + event.getTimestamp());
        System.out.println("===========================================");

        String title = "TASK UPDATED: " + event.getTitle();
        String body = event.getDescription() != null ? event.getDescription() : "A task has been updated!";

        Map<String, String> data = new HashMap<>();
        data.put("taskId", event.getTaskId().toString());
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
    public void handleTaskDeleted(TaskDeletedEvent event) {
        System.out.println("===========================================");
        System.out.println("TASK DELETED!:");
        System.out.println("    Task ID: " + event.getTaskId());
        System.out.println("    Title: " + event.getTitle());
        System.out.println("    Group: " + event.getGroupName());
        System.out.println("    Deleted By: " + event.getDeleterName());
        System.out.println("    Deleted At: " + event.getTimestamp());
        System.out.println("===========================================");

        String title = "TASK DELETED!";
        String body = "Task \"" + event.getTitle() + "\" has been deleted by " + event.getDeleterName()
                + " from group " +  event.getGroupName();

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
    public void handleTaskUserAssigned(TaskUserAssignedEvent event) {
        System.out.println("===========================================");
        System.out.println("USERS ASSIGNED TO TASK!:");
        System.out.println("    Task ID: " + event.getTaskId());
        System.out.println("    Title: " + event.getTitle());
        System.out.println("    Assigned Users: " + event.getAssignedUserIds());
        System.out.println("    Assigned By: " + event.getAssignerName());
        System.out.println("    Assigned At: " + event.getTimestamp());
        System.out.println("===========================================");

        List<String> tokens = event.getAssignedUserTokens();

        if (tokens.isEmpty()) {
            System.out.println("No FCM tokens available for assigned users. Skipping notification.");
            return;
        }

        String title = "ASSIGNED TO TASK!";
        String body = event.getAssignerName() + "assigned you to: \"" + event.getTitle() + "\"";

        Map<String, String> data = new HashMap<>();
        data.put("taskId", event.getTaskId().toString());
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
    public void handleTaskUserRemoved(TaskUserRemovedEvent event) {
        System.out.println("===========================================");
        System.out.println("USERS REMOVED FROM TASK!:");
        System.out.println("    Task ID: " + event.getTaskId());
        System.out.println("    Title: " + event.getTitle());
        System.out.println("    Removed Users: " + event.getRemovedUserIds());
        System.out.println("    Removed By: " + event.getRemoverId());
        System.out.println("    Removed At: " + event.getTimestamp());
        System.out.println("===========================================");

        List<String> tokens = event.getRemovedUserTokens();

        if (tokens.isEmpty()) {
            System.out.println("No FCM tokens available for removed users. Skipping notification.");
            return;
        }

        String title = "UNASSIGNED FROM TASK.";
        String body = event.getRemoverName() + " removed you from \"" + event.getTitle() + "\"";

        Map<String, String> data = new HashMap<>();
        data.put("taskId", event.getTaskId().toString());
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
    public void handleTaskStatusUpdated(TaskStatusUpdatedEvent event) {
        System.out.println("===========================================");
        System.out.println("TASK STATUS UPDATED!:");
        System.out.println("    Task ID: " + event.getTaskId());
        System.out.println("    Title: " + event.getTitle());
        System.out.println("    Updated By: " + event.getUpdaterName());
        System.out.println("    Updated At: " + event.getTimestamp());
        System.out.println("    Status: " + event.getStatus());
        System.out.println("===========================================");


        String title = "TASK STATUS UPDATED!.";
        String body = event.getUpdaterName() + " marked \"" + event.getTitle() + "\"" + " as " + event.getStatus().replace("_", " ");;

        Map<String, String> data = new HashMap<>();
        data.put("taskId", event.getTaskId().toString());
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


}
