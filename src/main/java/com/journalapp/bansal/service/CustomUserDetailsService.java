package com.journalapp.bansal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.journalapp.bansal.entity.User;
import com.journalapp.bansal.repository.UserRepo;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepo userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username);
        if (user != null) {
          UserDetails userDetails = org.springframework.security.core.userdetails.User.builder()
            // here we are using the builder pattern because we dont have .builder() method in the User class that we created, 
            // but we can use the builder pattern provided by Spring Security's User class to create a UserDetails object.
          .username(user.getUsername())
            .password(user.getPassword())
            .roles(user.getRoles().toArray(new String[0]))
            .build();
            return userDetails;
        }
        throw new UsernameNotFoundException("user not found with username: " + username);
    }
}
