package org.russel.komandosb.data.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.russel.komandosb.data.entity.TaskData;
import org.russel.komandosb.data.entity.UserData;
import org.russel.komandosb.data.enums.EventType;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskUserAssignedEvent {
    private EventType eventType;
    private Integer taskId;
    private Integer assignerId;
    private String assignerName;
    private String title;
    private List<Integer> assignedUserIds;
    private List<String> assignedUserTokens;
    private OffsetDateTime timestamp;

    public static TaskUserAssignedEvent from(TaskData task, UserData assigner, List<Integer> assignedUserIds, List<String> tokens) {
        return new TaskUserAssignedEvent(
                EventType.TASK_USER_ASSIGNED,
                task.getId(),
                assigner.getId(),
                assigner.getFullName(),
                task.getTitle(),
                assignedUserIds,
                tokens,
                OffsetDateTime.now(ZoneOffset.UTC)
        );
    }
}
