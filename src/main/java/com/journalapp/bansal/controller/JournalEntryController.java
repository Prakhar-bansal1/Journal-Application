package com.journalapp.bansal.controller;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.RestController;
import com.journalapp.bansal.entity.JournalEntry;
import com.journalapp.bansal.entity.User;
import com.journalapp.bansal.service.JournalEntryService;
import com.journalapp.bansal.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/journal")
public class JournalEntryController {

    @Autowired
    public JournalEntryService journalEntryService;

    @Autowired
    public UserService userService;
    
    @GetMapping("/abc/{username}")
    public ResponseEntity<?> getAllJournalEntriesOfUser(@PathVariable String username) {
            User user = userService.findByUsername(username);
            List<JournalEntry> all = user.getJournalEntries();
            return new ResponseEntity<>(all, HttpStatus.OK);
    }
          
    @PostMapping("/{username}")
    public ResponseEntity<JournalEntry> createEntryForUser(@PathVariable String username, @RequestBody JournalEntry myEntry) {
        try {
            // STEP 1: Find the user by username
            User user = userService.findByUsername(username);
            // STEP 4: Set the current timestamp on the entry
            myEntry.setDate(LocalDateTime.now());
            // STEP 5: Add the new entry to the user's journal entries collection
            user.getJournalEntries().add(myEntry);
            // STEP 6: Save the updated user (with the new entry) to database
            userService.saveEntry(user);
            // STEP 7: Return 201 CREATED with the created entry
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Handle illegal arguments - return 400 Bad Request
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Handle any other unexpected errors - return 500 Internal Server Error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/id/{myid}")
    public JournalEntry getEntryById(@PathVariable Long myid) {
        return journalEntryService.getEntryById(myid).orElse(null);
    }

    @DeleteMapping("/delete/{username}/{myid}")
    public ResponseEntity<?> deleteEntry(@PathVariable String username, @PathVariable Long myid) {
        try {
           
            boolean deleted = journalEntryService.deleteByIdAndUsername(myid, username);
           
            if(deleted){
               
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            } else {
               
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
            }
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
