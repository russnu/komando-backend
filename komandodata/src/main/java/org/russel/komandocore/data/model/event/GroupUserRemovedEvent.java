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
public class GroupUserRemovedEvent {
    private EventType eventType;
    private Integer groupId;
    private String groupName;
    private Integer removerId;
    private String removerName;
    private List<Integer> removedUserIds;
    private List<String> removedUserTokens;
    private OffsetDateTime timestamp;

    public static GroupUserRemovedEvent from(GroupData group, UserData remover, List<Integer> removedUserIds, List<String> tokens) {
        return new GroupUserRemovedEvent(
                EventType.GROUP_USER_REMOVED,
                group.getId(),
                group.getName(),
                remover.getId(),
                remover.getFullName(),
                removedUserIds,
                tokens,
                OffsetDateTime.now(ZoneOffset.UTC)
        );
    }
}
