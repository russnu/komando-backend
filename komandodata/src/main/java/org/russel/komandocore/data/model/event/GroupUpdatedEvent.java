package org.russel.komandocore.data.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.russel.komandocore.data.entity.GroupData;
import org.russel.komandocore.data.entity.UserData;
import org.russel.komandocore.data.enums.EventType;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUpdatedEvent {
    private EventType eventType;
    private Integer groupId;
    private String groupName;
    private Integer updaterId;
    private String updaterName;
    private List<Integer> users;
    private OffsetDateTime timestamp;

    public static GroupUpdatedEvent from(GroupData group, UserData updater){
        return new GroupUpdatedEvent(
                EventType.GROUP_UPDATED,
                group.getId(),
                group.getName(),
                updater.getId(),
                updater.getFullName(),
                group.getUsers() != null
                        ? group.getUsers().stream().map(UserData::getId).toList()
                        : null,
                group.getUpdatedAt().atOffset(ZoneOffset.UTC)
        );
    }
}
