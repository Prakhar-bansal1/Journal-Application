package com.journalapp.bansal.service;

import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.journalapp.bansal.entity.JournalEntry;
import com.journalapp.bansal.entity.User;
import com.journalapp.bansal.repository.JournalEntryRepo;

@Service
public class JournalEntryService {
   
    @Autowired
    private JournalEntryRepo journalEntryRepo;
    
    @Autowired
    private UserService userService;
   
    // SAVE ENTRY FOR USER - Creates and saves a new journal entry for a specific user
    @Transactional
    public void saveEntry(JournalEntry journalEntry, String username){
        // STEP 1: Find the user by username
        User user = userService.findByUsername(username);
        // STEP 2: Validate user exists
        if(user == null){
            throw new IllegalArgumentException("User not found");
        }
        // STEP 3: Set current timestamp on the entry
        journalEntry.setDate(LocalDateTime.now());
        // STEP 4: Associate this entry with the user
        journalEntry.setUser(user);
        // STEP 5: Save the entry to database
        JournalEntry saved = journalEntryRepo.save(journalEntry);
        // STEP 6: Add the saved entry to user's journal entries collection
        user.getJournalEntries().add(saved);
        // STEP 7: Update the user in database (to reflect the new entry in their collection)
        userService.saveEntry(user);
    }

    // SAVE ENTRY - Simple save operation without user association
    public void saveEntry(JournalEntry journalEntry){
        // Save the entry directly to database
        if (journalEntry != null) {
            journalEntryRepo.save(journalEntry);
        }
    }

    // GET ENTRY BY ID - Retrieves a journal entry by its unique identifier
    public Optional<JournalEntry> getEntryById(Long id){
        // Returns Optional - may be empty if entry with given ID doesn't exist
        if (id != null) {
            return journalEntryRepo.findById(id);
        }
        return Optional.empty();
    }

    // DELETE ENTRY BY ID AND USERNAME - Securely deletes an entry only if it belongs to the specified user
    public boolean deleteByIdAndUsername(Long id, String username){
        // STEP 1: CHECK USER - Verify that the user exists in the database
        if (id == null) return false;
        User user = userService.findByUsername(username);
        if(user == null){
            return false;  // User not found, cannot proceed with deletion
        }
        
        // STEP 2: FIND ENTRY - Try to find the journal entry by its ID
        Optional<JournalEntry> entryOpt = journalEntryRepo.findById(id);
        if(entryOpt.isEmpty()){
            return false;  // Entry does not exist, nothing to delete
        }
        
        // STEP 3: EXTRACT ENTRY - Get the actual entry object from Optional
        JournalEntry entry = entryOpt.get();
        
        // STEP 4: CHECK OWNERSHIP - Verify that the entry belongs to this specific user
        // This prevents users from deleting other users' entries
        if(!entry.getUser().getId().equals(user.getId())){
            return false;  // Security check failed - entry belongs to a different user
        }
        
        // STEP 5: REMOVE FROM USER - Delete the entry from the user's journal entries collection
        user.getJournalEntries().remove(entry);
        
        // STEP 6: UPDATE USER IN DB - Save the updated user (without this entry)
        userService.saveEntry(user);
        
        // STEP 7: DELETE FROM DATABASE - Remove the entry completely from the database
        journalEntryRepo.deleteById(id);
        
        // STEP 8: RETURN SUCCESS
        return true;
    }
}
