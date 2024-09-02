package com.example.demo.controllers;

import com.example.demo.entity.User;
import com.example.demo.requests.UserRequest;
import com.example.demo.response.UserResponse;
import com.example.demo.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponse> registerUser(
            @RequestParam("userName") String userName,
            @RequestParam("password") String password,
            @RequestParam("surname") String surname,
            @RequestParam("birthDate") String birthDate,
            @RequestParam("zodiac") String zodiac,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar) {
        try {
            User newUser = new User();
            newUser.setUserName(userName);
            newUser.setPassword(password);
            newUser.setSurname(surname);
            newUser.setBirthDate(birthDate);
            newUser.setZodiac(zodiac);

            if (profileImage != null && !profileImage.isEmpty()) {
                newUser.setProfileImage(profileImage.getBytes());
            }

            if (avatar != null && !avatar.isEmpty()) {
                newUser.setAvatar(avatar.getBytes());
            }

            User registeredUser = userService.registerUser(newUser);
            return new ResponseEntity<>(new UserResponse(registeredUser), HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (IOException e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> loginUser(@RequestBody UserRequest userRequest) {
        User user = userService.loginUser(userRequest.getUserName(), userRequest.getPassword());
        if (user != null) {
            return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<User> users = userService.getAllUsers();
        List<UserResponse> userResponses = users.stream()
                .map(UserResponse::new)
                .toList();
        return new ResponseEntity<>(userResponses, HttpStatus.OK);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponse> getOneUserById(@PathVariable Long userId) {
        User user = userService.getOneUserById(userId);
        if (user != null) {
            return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PutMapping("/{userId}")
    public ResponseEntity<UserResponse> updateOneUser(
            @PathVariable Long userId,
            @RequestParam("userName") String userName,
            @RequestParam("surname") String surname,
            @RequestParam("birthDate") String birthDate,
            @RequestParam("zodiac") String zodiac,
            @RequestParam(value = "profileImage", required = false) MultipartFile profileImage,
            @RequestParam(value = "avatar", required = false) MultipartFile avatar,
            @RequestParam(value = "password", required = false) String password) {
        User existingUser = userService.getOneUserById(userId);
        if (existingUser != null) {
            User updatedUser = new User();
            updatedUser.setId(userId);
            updatedUser.setUserName(userName);
            updatedUser.setSurname(surname);
            updatedUser.setBirthDate(birthDate);
            updatedUser.setZodiac(zodiac);

            if (profileImage != null && !profileImage.isEmpty()) {
                try {
                    updatedUser.setProfileImage(profileImage.getBytes());
                } catch (IOException e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            if (avatar != null && !avatar.isEmpty()) {
                try {
                    updatedUser.setAvatar(avatar.getBytes());
                } catch (IOException e) {
                    return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
                }
            }

            if (password != null && !password.isEmpty()) {
                updatedUser.setPassword(password);
            } else {
                updatedUser.setPassword(existingUser.getPassword());
            }

            User user = userService.updateOneUser(userId, updatedUser);
            if (user != null) {
                return new ResponseEntity<>(new UserResponse(user), HttpStatus.OK);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteOneUser(@PathVariable Long userId) {
        try {
            userService.deleteById(userId);
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}