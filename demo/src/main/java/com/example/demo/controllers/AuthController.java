package com.example.demo.controllers;

import com.example.demo.entity.User;
import com.example.demo.response.AuthResponse;
import com.example.demo.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody User user) {
        User savedUser = userService.registerUser(user.getUserName(), user.getPassword());
        if (savedUser != null) {
            AuthResponse response = new AuthResponse();
            response.setMessage("User registered successfully");
            response.setUserId(savedUser.getId());
            response.setAccessToken("dummyAccessToken"); // Gerçek token ile değiştirin
            response.setRefreshToken("dummyRefreshToken"); // Gerçek token ile değiştirin
            return ResponseEntity.ok(response);
        }
        AuthResponse response = new AuthResponse();
        response.setMessage("Error registering user");
        return ResponseEntity.status(500).body(response);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody User user) {
        User loggedInUser = userService.loginUser(user.getUserName(), user.getPassword());
        if (loggedInUser != null) {
            AuthResponse response = new AuthResponse();
            response.setMessage("User logged in successfully");
            response.setUserId(loggedInUser.getId());
            response.setAccessToken("dummyAccessToken"); // Gerçek token ile değiştirin
            response.setRefreshToken("dummyRefreshToken"); // Gerçek token ile değiştirin
            return ResponseEntity.ok(response);
        }
        AuthResponse response = new AuthResponse();
        response.setMessage("Invalid credentials");
        return ResponseEntity.status(401).body(response);
    }
}
