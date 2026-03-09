package org.russel.komandosb.serviceimpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.russel.komandosb.data.entity.GroupData;
import org.russel.komandosb.data.entity.TaskData;
import org.russel.komandosb.data.entity.UserData;
import org.russel.komandosb.data.entity.UserDeviceData;
import org.russel.komandosb.data.enums.GroupOperation;
import org.russel.komandosb.data.model.Group;
import org.russel.komandosb.data.model.User;
import org.russel.komandosb.data.model.event.*;
import org.russel.komandosb.data.repository.GroupRepository;
import org.russel.komandosb.data.repository.UserDeviceRepository;
import org.russel.komandosb.data.repository.UserRepository;
import org.russel.komandosb.data.service.GroupService;
import org.russel.komandosb.data.transform.TransformGroup;
import org.russel.komandosb.data.transform.TransformUser;
import org.russel.komandosb.kafka.producer.GroupEventProducer;
import org.russel.komandosb.kafka.producer.TaskEventProducer;
import org.russel.komandosb.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class GroupServiceImpl implements GroupService {
    @Autowired
    private GroupRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private GroupEventProducer groupEventProducer;
    @Autowired
    private UserDeviceRepository userDeviceRepository;
    //===================================================================================================//
    @Override
    public List<Group> getAll() {
        try {
            return repository.findAllWithCounts()
                    .stream()
                    .map(row -> {
                        Group g = new Group();
                        g.setId((Integer) row[0]);
                        g.setName((String) row[1]);
                        g.setUserCount(((Long) row[2]).intValue());
                        g.setTaskCount(((Long) row[3]).intValue());
                        return g;
                    })
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("\nERROR RETRIEVING GROUPS: \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public Group get(Integer id) {
        GroupData data = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("GROUP NOT FOUND.\n"));
        try {
            return TransformGroup.toDTO(data, true, true);
        } catch (Exception e) {
            throw new RuntimeException("\nERROR RETRIEVING GROUP WITH ID \"" + id + "\": \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public Group create(Group group, UserData currentUser) {
        try {
            GroupData entity = TransformGroup.toEntity(group);
            entity.setCreatedBy(currentUser);
            entity.getUsers().add(currentUser);

            if (group.getUsers() != null && !group.getUsers().isEmpty()){
                Set<Integer> userIds = group.getUsers()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toSet());

                List<UserData> users = userRepository.findAllById(userIds);

                if (users.size() != userIds.size()) {
                    throw new EntityNotFoundException("User/s not found.\n");
                }

                entity.getUsers().addAll(users);


            }

            GroupData saved = repository.save(entity);

            Set<Integer> addedUserIds = entity.getUsers().stream()
                    .map(UserData::getId)
                    .filter(id -> !id.equals(currentUser.getId())) // exclude creator
                    .collect(Collectors.toSet());

            if (!addedUserIds.isEmpty()){
                List<String> tokens = userDeviceRepository.findAllByUserIdIn(addedUserIds)
                        .stream()
                        .map(UserDeviceData::getFcmToken)
                        .filter(Objects::nonNull)
                        .toList();

                if (!tokens.isEmpty()) {
                    GroupUserAssignedEvent event = GroupUserAssignedEvent.from(
                            entity,
                            currentUser,
                            new ArrayList<>(addedUserIds),
                            tokens
                    );

                    groupEventProducer.publish(event);
                }
            }

            return TransformGroup.toDTO(saved, false, false);
        } catch (Exception e) {
            throw new RuntimeException("\nERROR CREATING GROUP: \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public Group update(Integer id, Group group, UserData currentUser) {
        // Get the existing group from db
        GroupData existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("GROUP WITH ID \"" + id + "\" NOT FOUND.\n"));
        permissionService.verifyGroup(existing, currentUser, GroupOperation.EDIT);
        try {
            existing.setName(group.getName());
            GroupData saved = repository.save(existing);

            GroupUpdatedEvent event = GroupUpdatedEvent.from(saved, currentUser);
            groupEventProducer.publish(event);
            return TransformGroup.toDTO(existing, false, false);
        } catch (Exception e) {
            throw new RuntimeException("\nERROR UPDATING GROUP WITH ID \"" + id + "\": \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public void delete(Integer id, UserData currentUser) {
        // Check group existence
        GroupData deleted = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("GROUP NOT FOUND.\n"));

        // Check permission
        permissionService.verifyGroup(deleted, currentUser, GroupOperation.DELETE);

        try {
            // Delete group
            repository.deleteById(id);

            // Create event and publish
            GroupDeletedEvent event = GroupDeletedEvent.from(deleted, currentUser);
            groupEventProducer.publish(event);
        } catch (Exception e) {
            throw new RuntimeException("\nERROR DELETING GROUP WITH ID \"" + id + "\": \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public void addUsers(Integer groupId, List<Integer> userIds, UserData currentUser) {
        // Check group existence
        GroupData data = repository.findById(groupId).orElseThrow(() -> new EntityNotFoundException("GROUP WITH ID \"" + groupId + "\" NOT FOUND.\n"));

        // Check permission
        permissionService.verifyGroup(data, currentUser, GroupOperation.ADD_USER);

        try {
            Set<Integer> uniqueIds = new HashSet<>(userIds);
            List<UserData> selectedUsers = userRepository.findAllById(uniqueIds);

            if (selectedUsers.size() != uniqueIds.size()) {
                throw new EntityNotFoundException("\nUser/s not found.\n");
            }

            data.getUsers().addAll(selectedUsers);

            repository.save(data);

            // Fetch FCM tokens for assigned users
            List<String> tokens = userDeviceRepository.findAllByUserIdIn(uniqueIds)
                    .stream()
                    .map(UserDeviceData::getFcmToken)
                    .filter(Objects::nonNull)
                    .toList();

            // Create event and publish
            GroupUserAssignedEvent event = GroupUserAssignedEvent.from(data, currentUser, new ArrayList<>(uniqueIds), tokens);
            groupEventProducer.publish(event);

        } catch (Exception e) {
            throw new RuntimeException("\nERROR ASSIGNING USERS TO GROUP WITH ID \"" + groupId + "\": \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public void removeUsersFromGroup(Integer groupId, List<Integer> userIds, UserData currentUser) {
        GroupData data = repository.findById(groupId).orElseThrow(() -> new EntityNotFoundException("GROUP WITH ID \"" + groupId + "\" NOT FOUND.\n"));
        permissionService.verifyGroup(data, currentUser, GroupOperation.REMOVE_USER);
        try {
            Set<Integer> uniqueIds = new HashSet<>(userIds);

            if (uniqueIds.contains(data.getCreatedBy().getId())) {
                throw new IllegalArgumentException("Group creator cannot be removed from the group.");
            }
            List<UserData> selectedUsers = userRepository.findAllById(uniqueIds);

            if (selectedUsers.size() != uniqueIds.size()) {
                throw new EntityNotFoundException("\nUser/s not found.\n");
            }

            data.getUsers().removeAll(selectedUsers);

            // Unassign removed users from all tasks in this group
            data.getTasks().forEach(task ->
                    task.getAssignedUsers().removeAll(selectedUsers)
            );

            repository.save(data);

            // Fetch FCM tokens for removed users
            List<String> tokens = userDeviceRepository.findAllByUserIdIn(uniqueIds)
                    .stream()
                    .map(UserDeviceData::getFcmToken)
                    .filter(Objects::nonNull)
                    .toList();

            // Create event and publish
            GroupUserRemovedEvent event = GroupUserRemovedEvent.from(data, currentUser, new ArrayList<>(uniqueIds), tokens);
            groupEventProducer.publish(event);

        } catch (Exception e) {
            throw new RuntimeException("\nERROR REMOVING MEMBERS FROM GROUP WITH ID \"" + groupId + "\": \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public List<User> getMembers(Integer groupId) {
        GroupData data = repository.findById(groupId).orElseThrow(() -> new EntityNotFoundException("GROUP WITH ID \"" + groupId + "\" NOT FOUND.\n"));
        try {
            return data.getUsers()
                    .stream()
                    .map(TransformUser::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("\nERROR RETRIEVING MEMBERS IN GROUP WITH ID \"" + groupId + "\": \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public List<Group> getGroupsByUser(Integer userId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("USER WITH ID \"" + userId + "\" NOT FOUND.\n"));
        try {
            return repository.findByUserIdWithCounts(userId)
                    .stream()
                    .map(row -> {
                        Group g = new Group();
                        g.setId((Integer) row[0]);
                        g.setName((String) row[1]);

                        User creator = new User();
                        creator.setId((Integer) row[2]);
                        creator.setFirstName((String) row[3]);
                        creator.setLastName((String) row[4]);

                        g.setCreatedBy(creator);

                        g.setUserCount(((Long) row[5]).intValue());
                        g.setTaskCount(((Long) row[6]).intValue());
                        return g;
                    })
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("\nERROR RETRIEVING GROUPS OF USER WITH ID \"" + userId + "\": \n", e);
        }
    }


}
