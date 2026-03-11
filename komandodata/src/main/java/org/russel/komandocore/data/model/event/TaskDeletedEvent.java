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
public class TaskDeletedEvent {
    private EventType eventType;
    private Integer taskId;
    private Integer deleterId;
    private String deleterName;
    private Integer groupId;
    private String groupName;
    private String title;
    private OffsetDateTime timestamp;

    public static TaskDeletedEvent from(TaskData task, UserData deleter){
        return new TaskDeletedEvent(
                EventType.TASK_DELETED,
                task.getId(),
                deleter.getId(),
                deleter.getFullName(),
                task.getGroup().getId(),
                task.getGroup().getName(),
                task.getTitle(),
                OffsetDateTime.now(ZoneOffset.UTC)
        );
    }
}
