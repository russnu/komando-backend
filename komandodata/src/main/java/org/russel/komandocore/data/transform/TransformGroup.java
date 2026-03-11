package org.russel.komandocore.data.transform;

import org.russel.komandocore.data.entity.GroupData;
import org.russel.komandocore.data.model.Group;
import org.russel.komandocore.data.model.Task;
import org.russel.komandocore.data.model.User;

import java.util.List;

public class TransformGroup {
    public static Group toDTO(GroupData data, boolean includeUsers, boolean includeTasks) {
        if (data == null) return null;
        Group group = new Group();
        group.setId(data.getId());
        group.setName(data.getName());
        group.setCreatedBy(TransformUser.toDTO(data.getCreatedBy()));
        group.setUserCount(data.getUsers().size());

        if (includeUsers) {
            List<User> users = data.getUsers()
                    .stream()
                    .map(TransformUser::toDTO)
                    .toList();
            group.setUsers(users);
        }

        if (includeTasks) {
            List<Task> tasks = data.getTasks()
                    .stream()
                    .map(taskData -> TransformTask.toDTO(taskData, false))
                    .toList();
            group.setTasks(tasks);
        }
        return group;
    }

    public static GroupData toEntity(Group dto) {
        if (dto == null) return null;
        GroupData groupData = new GroupData();
        groupData.setId(dto.getId());
        groupData.setName(dto.getName());
        return groupData;
    }
}
