package org.russel.komandosb.data.service;

import org.russel.komandosb.data.entity.UserData;
import org.russel.komandosb.data.model.Group;
import org.russel.komandosb.data.model.User;

import java.util.List;

public interface GroupService {
    List<Group> getAll();
    Group get(Integer id);
    Group create(Group group, UserData currentUser);
    Group update(Integer id, Group group, UserData currentUser);
    void delete(Integer id, UserData currentUser);

    // Add users to a group
    void addUsers(Integer groupId, List<Integer> userIds, UserData currentUser);

    // Remove users from a group
    void removeUsersFromGroup(Integer groupId, List<Integer> userIds, UserData currentUser);

    // Get all users in a group
    List<User> getMembers(Integer groupId);

    // Find all groups where a specific user is a member
    List<Group> getGroupsByUser(Integer userId);
}
