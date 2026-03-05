package org.russel.komandosb.serviceimpl;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.russel.komandosb.data.entity.GroupData;
import org.russel.komandosb.data.entity.TaskData;
import org.russel.komandosb.data.entity.UserData;
import org.russel.komandosb.data.entity.UserDeviceData;
import org.russel.komandosb.data.enums.GroupOperation;
import org.russel.komandosb.data.enums.Status;
import org.russel.komandosb.data.enums.TaskOperation;
import org.russel.komandosb.data.model.Task;
import org.russel.komandosb.data.model.User;
import org.russel.komandosb.data.model.event.*;
import org.russel.komandosb.data.repository.GroupRepository;
import org.russel.komandosb.data.repository.TaskRepository;
import org.russel.komandosb.data.repository.UserDeviceRepository;
import org.russel.komandosb.data.repository.UserRepository;
import org.russel.komandosb.data.service.TaskService;
import org.russel.komandosb.data.transform.TransformTask;
import org.russel.komandosb.data.transform.TransformUser;
import org.russel.komandosb.kafka.producer.TaskEventProducer;
import org.russel.komandosb.permission.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Transactional
@Service
public class TaskServiceImpl implements TaskService {
    @Autowired
    private TaskRepository repository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private UserDeviceRepository userDeviceRepository;
    @Autowired
    private TaskEventProducer taskEventProducer;
    @Autowired
    private PermissionService permissionService;
    //===================================================================================================//
    @Override
    public List<Task> getAll() {
        try {
            return repository.findAll()
                    .stream()
                    .map(taskData -> TransformTask.toDTO(taskData, false))
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new RuntimeException("ERROR RETRIEVING TASKS: \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public Task get(Integer id) {
        // Check task existence
        TaskData data = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("\nERROR RETRIEVING TASK WITH ID \"" + id + "\": \n"));
        try {
            return TransformTask.toDTO(data, true);
        } catch (Exception e) {
            throw new RuntimeException("ERROR RETRIEVING TASK WITH ID [" + id + "]: \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public Task create(Task task, UserData currentUser) {
        GroupData group = groupRepository.findById(task.getGroup().getId())
                .orElseThrow(() -> new EntityNotFoundException("\nGROUP WITH ID \"" + task.getGroup().getId() + "\" NOT FOUND.\n"));

        permissionService.verifyGroup(group, currentUser, GroupOperation.ADD_TASK);

        try {
            TaskData entity = TransformTask.toEntity(task);
            entity.setGroup(group);
            entity.setCreatedBy(currentUser);
            entity.setStatus(Status.PENDING);

            if (task.getAssignedUsers() != null && !task.getAssignedUsers().isEmpty()){
                Set<Integer> userIds = task.getAssignedUsers()
                        .stream()
                        .map(User::getId)
                        .collect(Collectors.toSet());

                List<UserData> users = userRepository.findAllById(userIds);

                if (users.size() != userIds.size()) {
                    throw new EntityNotFoundException("User/s not found.");
                }


                entity.getAssignedUsers().addAll(users);
            }

            TaskData saved = repository.save(entity);

            // Create event and publish
            TaskCreatedEvent event = TaskCreatedEvent.from(saved);
            taskEventProducer.publish(event);

            return TransformTask.toDTO(saved, false);
        } catch (Exception e) {
            throw new RuntimeException("\nERROR CREATING TASK:\n", e);
        }
    }
    //===================================================================================================//
    @Override
    public Task update(Integer id, Task task, UserData currentUser) {
        // Get the existing task from db
        TaskData existing = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("\nTASK WITH ID \"" + id + "\" NOT FOUND.\n"));

        permissionService.verifyTask(existing, currentUser, TaskOperation.EDIT);

        try {
            // Set new task details to the existing task
            existing.setTitle(task.getTitle());
            existing.setDescription(task.getDescription());
            TaskData saved = repository.save(existing);

            // Create event and publish
            TaskUpdatedEvent event = TaskUpdatedEvent.from(saved, currentUser);
            taskEventProducer.publish(event);

            return TransformTask.toDTO(existing, false);
        } catch (Exception e) {
            throw new RuntimeException("\nERROR UPDATING TASK WITH ID \"" + id + "\":\n", e);
        }
    }
    //===================================================================================================//
    @Override
    public void delete(Integer id, UserData currentUser) {
        // Check task existence
        TaskData deleted = repository.findById(id).orElseThrow(() -> new EntityNotFoundException("\nTASK NOT FOUND.\n"));

        permissionService.verifyTask(deleted, currentUser, TaskOperation.DELETE);

        try {

            // Delete task
            repository.deleteById(id);

            // Create event and publish
            TaskDeletedEvent event = TaskDeletedEvent.from(deleted, currentUser);
            taskEventProducer.publish(event);

        } catch (Exception e) {
            throw new RuntimeException("\nERROR DELETING TASK WITH ID \"" + id + "\": \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public void assignUsers(Integer taskId, UserData currentUser, List<Integer> userIds) {
        // Check task existence
        TaskData data = repository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("\nTASK WITH ID \"" + taskId + "\" NOT FOUND.\n"));

        // Check permission
        permissionService.verifyTask(data, currentUser, TaskOperation.ASSIGN_USER);

        try {
            Set<Integer> uniqueIds = new HashSet<>(userIds);
            List<UserData> selectedUsers = userRepository.findAllById(uniqueIds);

            if (selectedUsers.size() != uniqueIds.size()) {
                throw new EntityNotFoundException("\nUser/s not found.\n");
            }

            data.getAssignedUsers().addAll(selectedUsers);

            repository.save(data);

            // Fetch FCM tokens for assigned users
            List<String> tokens = userDeviceRepository.findAllByUserIdIn(uniqueIds)
                    .stream()
                    .map(UserDeviceData::getFcmToken)
                    .filter(Objects::nonNull)
                    .toList();

            TaskUserAssignedEvent event = TaskUserAssignedEvent.from(data, currentUser, new ArrayList<>(uniqueIds), tokens);
            taskEventProducer.publish(event);

        } catch (Exception e) {
            throw new RuntimeException("\nERROR ASSIGNING USERS TO TASK WITH ID \"" + taskId + "\": \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public void removeUsers(Integer taskId, UserData currentUser, List<Integer> userIds) {
        TaskData data = repository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("\nTASK WITH ID \"" + taskId + "\" NOT FOUND.\n"));
        permissionService.verifyTask(data, currentUser, TaskOperation.REMOVE_USER);

        try {
            Set<Integer> uniqueIds = new HashSet<>(userIds);
            List<UserData> selectedUsers = userRepository.findAllById(uniqueIds);

            if (selectedUsers.size() != uniqueIds.size()) {
                throw new EntityNotFoundException("\nUser/s not found.\n");
            }

            data.getAssignedUsers().removeAll(selectedUsers);

            repository.save(data);

            // Fetch FCM tokens for removed users
            List<String> tokens = userDeviceRepository.findAllByUserIdIn(uniqueIds)
                    .stream()
                    .map(UserDeviceData::getFcmToken)
                    .filter(Objects::nonNull)
                    .toList();

            TaskUserRemovedEvent event = TaskUserRemovedEvent.from(data, currentUser, new ArrayList<>(uniqueIds), tokens);
            taskEventProducer.publish(event);

        } catch (Exception e) {
            throw new RuntimeException("\nERROR REMOVING USERS FROM TASK WITH ID \"" + taskId + "\": \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public List<User> getAssignedUsers(Integer taskId) {
        TaskData data = repository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("\nTASK WITH ID \"" + taskId + "\" NOT FOUND.\n"));
        try {
            return data.getAssignedUsers()
                    .stream()
                    .map(TransformUser::toDTO)
                    .toList();
        } catch (Exception e) {
            throw new RuntimeException("ERROR RETRIEVING ASSIGNED USERS IN TASK WITH ID [" + taskId + "]: \n", e);
        }

    }
    //===================================================================================================//
    @Override
    public List<Task> getTasksByUser(Integer userId) {
        userRepository.findById(userId).orElseThrow(() -> new EntityNotFoundException("\nUSER WITH ID \"" + userId  + "\" NOT FOUND.\n"));
        try {
            return repository.findAllByAssignedUsers_Id(userId)
                    .stream()
                    .map(taskData -> TransformTask.toDTO(taskData, false))
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(
                    "ERROR RETRIEVING TASKS FOR USER WITH ID [" + userId + "]: \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public List<Task> getTasksByGroup(Integer groupId) {
        groupRepository.findById(groupId).orElseThrow(() -> new EntityNotFoundException("\nGROUP WITH ID \"" + groupId  + "\" NOT FOUND.\n"));
        try {
            return repository.findAllByGroup_Id(groupId)
                    .stream()
                    .map(taskData -> TransformTask.toDTO(taskData, false))
                    .toList();

        } catch (Exception e) {
            throw new RuntimeException(
                    "ERROR RETRIEVING TASKS FOR GROUP WITH ID [" + groupId + "]: \n", e);
        }
    }
    //===================================================================================================//
    @Override
    public Task updateStatus(Integer taskId, Status status, UserData currentUser) {
        // Get the existing task from db
        TaskData existing = repository.findById(taskId).orElseThrow(() -> new EntityNotFoundException("\nTASK WITH ID \"" + taskId + "\" NOT FOUND.\n"));

        permissionService.verifyTask(existing, currentUser, TaskOperation.UPDATE_STATUS);

        try {
            existing.setStatus(status);
            TaskData saved = repository.save(existing);

            TaskStatusUpdatedEvent event = TaskStatusUpdatedEvent.from(saved, currentUser);
            taskEventProducer.publish(event);

            return TransformTask.toDTO(existing, false);
        }
        catch(Exception e){
            throw new RuntimeException(
                    "\nERROR UPDATING TASK STATUS FOR TASK WITH ID \"" + taskId + "\": \n", e);
        }
    }
}
