package org.russel.komandosb.data.transform;

import org.russel.komandosb.data.entity.TaskData;
import org.russel.komandosb.data.model.Task;
import org.russel.komandosb.data.model.User;

import java.util.List;

public class TransformTask {
    public static Task toDTO(TaskData data, boolean includeUsers) {
        if (data == null) return null;
        Task task = new Task();
        task.setId(data.getId());
        task.setTitle(data.getTitle());
        task.setDescription(data.getDescription());
        task.setStatus(data.getStatus());
        task.setCreatedBy(TransformUser.toDTO(data.getCreatedBy()));
        task.setGroup(TransformGroup.toDTO(data.getGroup(), false, false));
        task.setCreatedAt(data.getCreatedAt());
        task.setUpdatedAt(data.getUpdatedAt());

        if (includeUsers){
            List<User> users = data.getAssignedUsers()
                    .stream()
                    .map(TransformUser::toDTO)
                    .toList();
            task.setAssignedUsers(users);
        }
        return task;

    }

    public static TaskData toEntity(Task dto) {
        if (dto == null) return null;
        TaskData taskData = new TaskData();
        taskData.setId(dto.getId());
        taskData.setTitle(dto.getTitle());
        taskData.setDescription(dto.getDescription());
        taskData.setStatus(dto.getStatus());
//        taskData.setCreatedBy(TransformUser.toEntity(dto.getCreatedBy()));
//        taskData.setGroup(TransformGroup.toEntity(dto.getGroup()));
        taskData.setCreatedAt(dto.getCreatedAt());
        taskData.setUpdatedAt(dto.getUpdatedAt());
        return taskData;
    }
}
