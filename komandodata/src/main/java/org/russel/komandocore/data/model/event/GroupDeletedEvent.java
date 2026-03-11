package org.russel.komandocore.data.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.russel.komandocore.data.entity.GroupData;
import org.russel.komandocore.data.entity.UserData;
import org.russel.komandocore.data.enums.EventType;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupDeletedEvent {
    private EventType eventType;
    private Integer groupId;
    private String groupName;
    private Integer deleterId;
    private String deleterName;
    private OffsetDateTime timestamp;

    public static GroupDeletedEvent from(GroupData group, UserData deleter){
        return new GroupDeletedEvent(
                EventType.GROUP_DELETED,
                group.getId(),
                group.getName(),
                deleter.getId(),
                deleter.getFullName(),
                OffsetDateTime.now(ZoneOffset.UTC)
        );
    }
}
