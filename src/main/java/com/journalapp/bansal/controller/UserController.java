package com.journalapp.bansal.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journalapp.bansal.entity.User;
import com.journalapp.bansal.service.UserService;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;

@RestController
@RequestMapping("/User")
public class UserController {
    @Autowired
    private UserService userService;

    // GET MAPPING: /User - Retrieve all users from the database
    // HTTP METHOD: GET
    // ENDPOINT: GET /User
    // RETURNS: List of all User objects
    @GetMapping
    public List<User> getAllUsers() {
        // Fetch and return all users from the database
        return userService.getAll();
    }

    // POST MAPPING: /User - Create a new user
    // HTTP METHOD: POST
    // ENDPOINT: POST /User
    // REQUEST BODY: User object with username and password
    // RETURNS: ResponseEntity with success message or error
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody User user){
        userService.saveNewUser(user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("User created successfully: " + user.getUsername());
    }

    @GetMapping("/test")
    public ResponseEntity<?> test(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        return ResponseEntity.ok("Authenticated as: " + authentication.getName());
    }
    
    // PUT MAPPING: /User/{username} - Update an existing user by username
    // HTTP METHOD: PUT
    // ENDPOINT: PUT /User/{username}
    // PATH VARIABLE: username - the username of the user to update
    // REQUEST BODY: User object with updated username and password
    // RETURNS: ResponseEntity with updated user (200 OK) or error message (404 Not Found)
    @PutMapping()
    public ResponseEntity<?> updateUser(@RequestBody User user){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        User userInDb = userService.findByUsername(username);
        userInDb.setUsername(user.getUsername());
        userInDb.setPassword(user.getPassword());
        userService.saveNewUser(userInDb);
        return ResponseEntity.status(HttpStatus.OK).body("User updated successfully");
    }

    @DeleteMapping()
    public ResponseEntity<?> deleteUser(){
        Authentication authentication= SecurityContextHolder.getContext().getAuthentication();
        String username=authentication.getName();
        userService.deleteByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body("User deleted successfully: " + username);
    }
}
