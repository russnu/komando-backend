package org.russel.komandosb.data.model.event;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.russel.komandosb.data.entity.GroupData;
import org.russel.komandosb.data.entity.UserData;
import org.russel.komandosb.data.enums.EventType;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GroupUserAssignedEvent {
    private EventType eventType;
    private Integer groupId;
    private String groupName;
    private Integer assignerId;
    private String assignerName;
    private List<Integer> assignedUserIds;
    private List<String> assignedUserTokens;
    private OffsetDateTime timestamp;

    public static GroupUserAssignedEvent from(GroupData group, UserData assigner, List<Integer> assignedUserIds, List<String> tokens) {
        return new GroupUserAssignedEvent(
                EventType.GROUP_USER_ASSIGNED,
                group.getId(),
                group.getName(),
                assigner.getId(),
                assigner.getFullName(),
                assignedUserIds,
                tokens,
                OffsetDateTime.now(ZoneOffset.UTC)
        );
    }
}
