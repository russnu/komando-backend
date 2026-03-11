package org.russel.komandocore.controller;

import org.russel.komandocore.data.entity.UserData;
import org.russel.komandocore.data.model.request.LoginRequest;
import org.russel.komandocore.data.model.User;
import org.russel.komandocore.data.model.response.LoginResponse;
import org.russel.komandocore.data.service.UserService;
import org.russel.komandocore.data.transform.TransformUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

@RequestMapping("/auth")
@RestController
public class AuthController {
    @Autowired
    private UserService service;

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }
    //========================================================================================================//
    @PostMapping("/register")
    public ResponseEntity<?> create(@RequestBody User user){
        User newUser = service.create(user);
        URI location = URI.create("/auth/register/" + newUser.getId());
        return ResponseEntity.created(location).body(newUser);
    }
    //========================================================================================================//
    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            loginRequest.getUsername(),
                            loginRequest.getPassword())
            );

            // If authentication is successful
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserData user = (UserData) authentication.getPrincipal();
            String fullName = TransformUser.toDTO(user).getFullName();

            LoginResponse response = new LoginResponse(
                    user.getId(),
                    fullName,
                    user.getUsername(),
                    "Login successful"
            );

            return ResponseEntity.ok(response);

        } catch (AuthenticationException ex) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Invalid username or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(null, null, null, "Invalid username or password"));
        }
    }

}
