package org.russel.komandocore.controller;

import org.russel.komandocore.data.model.User;
import org.russel.komandocore.data.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequestMapping("/api/user")
@RestController
public class UserController {
    @Autowired
    private UserService service;
    //========================================================================================================//
    @GetMapping
    public ResponseEntity<?> getAll(){
        List<User> users = service.getAll();
        return ResponseEntity.ok(users);
    }
    //========================================================================================================//
    @GetMapping("/{id}")
    public ResponseEntity<?> get(@PathVariable Integer id){
        User user = service.get(id);
        return ResponseEntity.ok(user);
    }
    //========================================================================================================//
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable Integer id, @RequestBody User user){
        User updated = service.update(id, user);
        return ResponseEntity.ok(updated);
    }
    //========================================================================================================//
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Integer id){
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
    //========================================================================================================//
}
