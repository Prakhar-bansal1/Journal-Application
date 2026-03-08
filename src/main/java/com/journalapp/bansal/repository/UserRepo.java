package com.journalapp.bansal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.journalapp.bansal.entity.User;

public interface UserRepo extends JpaRepository<User, Long>{
    User findByUsername(String username);
}
