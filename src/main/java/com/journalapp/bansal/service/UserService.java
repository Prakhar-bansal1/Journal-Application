package com.journalapp.bansal.service;


import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.journalapp.bansal.entity.User;
import com.journalapp.bansal.repository.UserRepo;

@Service
public class UserService {
    @Autowired
    private UserRepo userRepo;
    
    // SAVE USER - Saves or updates a user in the database
    public void saveEntry(User user){
        // Persist user object to database (INSERT if new, UPDATE if existing)
        if (user != null) {
            userRepo.save(user);
        }
    }

    // SAVE NEW USER - Saves a new user with encoded password
    private static final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
    public void saveNewUser(User user){
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRoles(Arrays.asList("USER"));
            userRepo.save(user);
    }

    // GET USER BY ID - Retrieves a user from the database using their unique identifier
    public Optional<User> getEntryById(Long id){
        // Returns Optional - may be empty if user with given ID doesn't exist
        if (id != null) {
            return userRepo.findById(id);
        }
        return Optional.empty();
    }

    // DELETE USER - Permanently removes a user from the database by their ID
    public void deleteById(Long id){
        // Delete the user record and all associated data from database
        if (id != null) {
            userRepo.deleteById(id);
        }
    }
    // delete user by username
    public void deleteByUsername(String username){
      User user = userRepo.findByUsername(username);
            if(username != null){
                if(user != null){
                      userRepo.delete(user);
                }
            }
    }

    // GET ALL USERS - Retrieves all users from the database
    public List<User> getAll(){
        // Fetch all user records and return as a List
        return userRepo.findAll();
    }

    // FIND USER BY USERNAME - Searches for a user using their username
    public User findByUsername(String username){
        // Query database for user with matching username
        // Returns the user object if found, null if not found
        return userRepo.findByUsername(username);
    }
}
