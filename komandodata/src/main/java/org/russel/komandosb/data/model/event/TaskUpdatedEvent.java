package org.russel.komandosb.data.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.russel.komandosb.data.entity.TaskData;
import org.russel.komandosb.data.entity.UserData;
import org.russel.komandosb.data.enums.EventType;
import org.russel.komandosb.data.enums.Status;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUpdatedEvent {
    private EventType eventType;
    private Integer taskId;
    private Integer updaterId;
    private String updateName;
    private Integer groupId;
    private String groupName;
    private String title;
    private String description;
    private String status;
    private List<Integer> assignedUsers;
    private OffsetDateTime timestamp;

    public static TaskUpdatedEvent from(TaskData task, UserData updater){
        return new TaskUpdatedEvent(
                EventType.TASK_UPDATED,
                task.getId(),
                updater.getId(),
                updater.getFullName(),
                task.getGroup().getId(),
                task.getGroup().getName(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus().name(),
                task.getAssignedUsers() != null
                        ? task.getAssignedUsers().stream().map(UserData::getId).toList()
                        : null,
                task.getUpdatedAt().atOffset(ZoneOffset.UTC)
        );
    }
}
