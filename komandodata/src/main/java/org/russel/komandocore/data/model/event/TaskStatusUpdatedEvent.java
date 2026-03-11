package org.russel.komandocore.data.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.russel.komandocore.data.entity.TaskData;
import org.russel.komandocore.data.entity.UserData;
import org.russel.komandocore.data.enums.EventType;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskStatusUpdatedEvent {
    private EventType eventType;
    private Integer taskId;
    private Integer updaterId;
    private String updaterName;
    private Integer groupId;
    private String groupName;
    private String title;
    private String status;
    private OffsetDateTime timestamp;

    public static TaskStatusUpdatedEvent from(TaskData task, UserData updater){
        return new TaskStatusUpdatedEvent(
                EventType.TASK_STATUS_UPDATED,
                task.getId(),
                updater.getId(),
                updater.getFullName(),
                task.getGroup().getId(),
                task.getGroup().getName(),
                task.getTitle(),
                task.getStatus().name(),
                task.getUpdatedAt().atOffset(ZoneOffset.UTC)
        );
    }
}
