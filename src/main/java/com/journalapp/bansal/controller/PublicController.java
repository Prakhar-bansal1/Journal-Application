package com.journalapp.bansal.controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.journalapp.bansal.entity.User;
import com.journalapp.bansal.service.UserService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/public")
public class PublicController{
    private UserService userService;

    @PostMapping("/create-user")
   public void createUser(@RequestBody User user){
    userService.saveEntry(user);
   }
}
