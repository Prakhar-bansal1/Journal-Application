package com.journalapp.bansal.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.journalapp.bansal.entity.JournalEntry;

public interface JournalEntryRepo extends JpaRepository<JournalEntry, Long>{
    
}