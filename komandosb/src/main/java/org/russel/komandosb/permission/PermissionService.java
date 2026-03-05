package org.russel.komandosb.permission;

import org.russel.komandosb.data.entity.GroupData;
import org.russel.komandosb.data.entity.TaskData;
import org.russel.komandosb.data.entity.UserData;
import org.russel.komandosb.data.enums.GroupOperation;
import org.russel.komandosb.data.enums.TaskOperation;
import org.springframework.stereotype.Service;

@Service
public class PermissionService {
    public void verifyTask(TaskData task, UserData currentUser, TaskOperation op) {
        boolean isTaskCreator = task.getCreatedBy().getId().equals(currentUser.getId());
        boolean isAssigned = task.getAssignedUsers()
                .stream()
                .anyMatch(u -> u.getId().equals(currentUser.getId()));


        switch (op) {
            case CREATE:
                // Only allowed via group creation context
                // Usually checked when creating task under a group
                throw new UnsupportedOperationException("Use \"verifyGroup\" for \"CREATE\" context.\n");
            case EDIT:
                if (!isTaskCreator) {
                    throw new RuntimeException("\nONLY TASK CREATOR CAN EDIT TASK DETAILS.\n");
                }
                break;
            case UPDATE_STATUS:
                if (!isAssigned) {
                    throw new RuntimeException("\nONLY ASSIGNED USERS CAN UPDATE TASK STATUS.\n");
                }
                break;
            case ASSIGN_USER:
            case REMOVE_USER:
            case DELETE:
                if (!isTaskCreator) {
                    throw new RuntimeException("\nONLY TASK CREATOR CAN ASSIGN/REMOVE USERS OR DELETE TASK.\n");
                }
                break;
            case VIEW:
                if (!isAssigned) {
                    throw new RuntimeException("\nONLY ASSIGNED USERS CAN VIEW FULL TASK DETAILS.\n");
                }
                break;
            default:
                throw new IllegalArgumentException("\nUnknown task operation.\n");
        }
    }
    public void verifyGroup(GroupData group, UserData currentUser, GroupOperation op) {

        boolean isGroupCreator = group.getUsers().stream() // TODO: change to getCreatedBy()
                .anyMatch(u -> u.getId().equals(currentUser.getId()));
        boolean isGroupMember = group.getUsers().stream()
                .anyMatch(u -> u.getId().equals(currentUser.getId()));
        switch (op) {
            case CREATE:
                break;
            case EDIT:
            case DELETE:
                if (!isGroupCreator) {
                    throw new RuntimeException("\nONLY GROUP CREATOR CAN EDIT DETAILS AND DELETE THE GROUP.\n");
                }
                break;
            case ADD_TASK:
                if (!isGroupMember) {
                    throw new RuntimeException("\nONLY GROUP MEMBERS CAN ADD TASKS ON THE GROUP.\n");
                }
                break;
            case ADD_USER:
            case REMOVE_USER:
                if (!isGroupCreator) {
                    throw new RuntimeException("\nONLY GROUP creator CAN PERFORM " + op + " ON THE GROUP.\n");
                }
                break;
            case VIEW_TASKS:
                boolean isMember = group.getUsers()
                        .stream()
                        .anyMatch(u -> u.getId().equals(currentUser.getId()));
                if (!isMember) {
                    throw new RuntimeException("\nONLY GROUP MEMBERS CAN VIEW GROUP TASKS.\n");
                }
                break;
            default:
                throw new IllegalArgumentException("\nUnknown group operation.\n");
        }
    }
}
