package org.russel.komandocore.controller;

import org.russel.komandocore.data.entity.UserData;
import org.russel.komandocore.data.enums.UpdateStatusRequest;
import org.russel.komandocore.data.model.Task;
import org.russel.komandocore.data.model.User;
import org.russel.komandocore.data.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/task")
@RestController
public class TaskController {
    @Autowired
    private TaskService service;
    //========================================================================================================//
    @GetMapping
    public ResponseEntity<?> getAll(){
        List<Task> tasks = service.getAll();
        return ResponseEntity.ok(tasks);
    }
    //========================================================================================================//
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id){
        Task task = service.get(id);
        return ResponseEntity.ok(task);
    }
    //========================================================================================================//
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Task task, @AuthenticationPrincipal UserData currentUser){
        Task newTask = service.create(task, currentUser);
        URI location = URI.create("/api/task/" + newTask.getId());
        return ResponseEntity.created(location).body(newTask);
    }
    //========================================================================================================//
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Task task, @AuthenticationPrincipal UserData currentUser){
        Task updated = service.update(id, task, currentUser);
        return ResponseEntity.ok(updated);
    }
    //========================================================================================================//
    @PatchMapping("/{taskId}/status")
    public ResponseEntity<?> updateStatus(@PathVariable Integer taskId, @RequestBody UpdateStatusRequest request, @AuthenticationPrincipal UserData currentUser){
        Task updated = service.updateStatus(taskId, request.status(), currentUser);
        return ResponseEntity.ok(updated);
    }
    //========================================================================================================//
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, @AuthenticationPrincipal UserData currentUser){
        service.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
    //========================================================================================================//
    @PutMapping("/{taskId}/users")
    public ResponseEntity<?> assignUsers(@PathVariable Integer taskId, @AuthenticationPrincipal UserData currentUser, @RequestBody List<Integer> userIds){
        service.assignUsers(taskId, currentUser, userIds);
        return ResponseEntity.noContent().build();
    }
    //========================================================================================================//
    @DeleteMapping("/{taskId}/users")
    public ResponseEntity<?> removeUsers(@PathVariable Integer taskId, @AuthenticationPrincipal UserData currentUser, @RequestBody List<Integer> userIds){
        service.removeUsers(taskId, currentUser, userIds);
        return ResponseEntity.noContent().build();
    }
    //========================================================================================================//
    @GetMapping("/{taskId}/users")
    public ResponseEntity<?> getAssignedUsers(@PathVariable Integer taskId){
        List<User> assignedUsers = service.getAssignedUsers(taskId);
        return ResponseEntity.ok(assignedUsers);
    }
    //========================================================================================================//
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getTasksByUser(@PathVariable Integer userId){
        List<Task> tasks = service.getTasksByUser(userId);
        return ResponseEntity.ok(tasks);
    }
    //========================================================================================================//
    @GetMapping("/group/{groupId}")
    public ResponseEntity<?> getTasksByGroup(@PathVariable Integer groupId){
        List<Task> tasks = service.getTasksByGroup(groupId);
        return ResponseEntity.ok(tasks);
    }

}
