package org.russel.komandosb.controller;

import org.russel.komandosb.data.entity.UserData;
import org.russel.komandosb.data.model.Group;
import org.russel.komandosb.data.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RequestMapping("/api/group")
@RestController
public class GroupController {
    @Autowired
    private GroupService service;
    //========================================================================================================//
    @GetMapping
    public ResponseEntity<?> getAll(){
        List<Group> groups = service.getAll();
        return ResponseEntity.ok(groups);
    }
    //========================================================================================================//
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id){
        Group group = service.get(id);
        return ResponseEntity.ok(group);
    }
    //========================================================================================================//
    @PostMapping
    public ResponseEntity<?> create(@RequestBody Group group, @AuthenticationPrincipal UserData currentUser){
        Group newGroup = service.create(group, currentUser);
        URI location = URI.create("/api/group/" + newGroup.getId());
        return ResponseEntity.created(location).body(newGroup);
    }
    //========================================================================================================//
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody Group group, @AuthenticationPrincipal UserData currentUser){
        Group updated = service.update(id, group, currentUser);
        return ResponseEntity.ok(updated);
    }
    //========================================================================================================//
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id, @AuthenticationPrincipal UserData currentUser){
        service.delete(id, currentUser);
        return ResponseEntity.noContent().build();
    }
    //========================================================================================================//
    @PutMapping("/{groupId}/users")
    public ResponseEntity<?> addUsers(@PathVariable Integer groupId, @RequestBody List<Integer> userIds, @AuthenticationPrincipal UserData currentUser){
        service.addUsers(groupId, userIds, currentUser);
        return ResponseEntity.noContent().build();
    }
    //========================================================================================================//
    @DeleteMapping("/{groupId}/users")
    public ResponseEntity<?> removeUsersFromGroup(@PathVariable Integer groupId, @RequestBody List<Integer> userIds, @AuthenticationPrincipal UserData currentUser){
        service.removeUsersFromGroup(groupId, userIds, currentUser);
        return ResponseEntity.noContent().build();
    }
    //========================================================================================================//
    @GetMapping("/user/{userId}")
    public ResponseEntity<?> getGroupsByUser(@PathVariable Integer userId){
        List<Group> groups = service.getGroupsByUser(userId);
        return ResponseEntity.ok(groups);
    }
    //========================================================================================================//

}
