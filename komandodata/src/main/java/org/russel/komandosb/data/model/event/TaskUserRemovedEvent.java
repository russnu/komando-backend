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
public class TaskUserRemovedEvent {
    private EventType eventType;
    private Integer taskId;
    private Integer removerId;
    private String removerName;
    private String title;
    private List<Integer> removedUserIds;
    private List<String> removedUserTokens;
    private OffsetDateTime timestamp;

    public static TaskUserRemovedEvent from(TaskData task, UserData remover,  List<Integer> removedUserIds, List<String> tokens) {
        return new TaskUserRemovedEvent(
                EventType.TASK_USER_REMOVED,
                task.getId(),
                remover.getId(),
                remover.getFullName(),
                task.getTitle(),
                removedUserIds,
                tokens,
                OffsetDateTime.now(ZoneOffset.UTC)
        );
    }
}
