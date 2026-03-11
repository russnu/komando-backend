package org.russel.komandocore.data.service;

import org.russel.komandocore.data.entity.UserData;
import org.russel.komandocore.data.enums.Status;
import org.russel.komandocore.data.model.Task;
import org.russel.komandocore.data.model.User;

import java.util.List;

public interface TaskService {
    List<Task> getAll();
    Task get(Integer id);
    Task create(Task task, UserData currentUser);
    Task update(Integer id, Task task, UserData currentUser);
    void delete(Integer id, UserData currentUser);

    // Assign users to a task
    void assignUsers(Integer taskId, UserData currentUser, List<Integer> userIds);

    // Remove users from a task
    void removeUsers(Integer taskId, UserData currentUser, List<Integer> userIds);

    // Fetch all users assigned to a task
    List<User> getAssignedUsers(Integer taskId);

    // Fetch all tasks assigned to a user
    List<Task> getTasksByUser(Integer userId);

    // Fetch all tasks in a group
    List<Task> getTasksByGroup(Integer groupId);

    Task updateStatus(Integer taskId, Status status, UserData currentUser);

}
