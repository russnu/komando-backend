package org.russel.komandocore.data.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.russel.komandocore.data.entity.TaskData;
import org.russel.komandocore.data.entity.UserData;
import org.russel.komandocore.data.enums.EventType;
import org.russel.komandocore.data.enums.Status;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskCreatedEvent{
    private EventType eventType;
    private Integer taskId;
    private Integer creatorId;
    private String creatorName;
    private Integer groupId;
    private String groupName;
    private String title;
    private String description;
    private Status status;
    private List<Integer> assignedUsers;
    private OffsetDateTime timestamp;

    public static TaskCreatedEvent from(TaskData task){
        return new TaskCreatedEvent(
                EventType.TASK_CREATED,
                task.getId(),
                task.getCreatedBy().getId(),
                task.getCreatedBy().getFullName(),
                task.getGroup().getId(),
                task.getGroup().getName(),
                task.getTitle(),
                task.getDescription(),
                task.getStatus(),
                task.getAssignedUsers() != null
                        ? task.getAssignedUsers().stream().map(UserData::getId).toList()
                        : null,
                task.getCreatedAt().atOffset(ZoneOffset.UTC)
        );
    }

}
