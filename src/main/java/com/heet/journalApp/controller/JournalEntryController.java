package com.heet.journalApp.controller;

import com.heet.journalApp.entity.JournalEntry;
import com.heet.journalApp.entity.User;
import com.heet.journalApp.service.JournalEntryService;
import com.heet.journalApp.service.UserService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/journal")
public class JournalEntryController {

//    dependency injection
    @Autowired
    private JournalEntryService journalEntryService;

    @Autowired
    private UserService userService;

    @GetMapping("/{username}")
    public ResponseEntity<List<JournalEntry>> getAllJournalEntriesOfUser(@PathVariable String username) {
        User user = userService.findByUserName(username);
        List<JournalEntry> li = user.getJournalEntryList();
        if(li!=null && !li.isEmpty()){
            return new ResponseEntity<>(li, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PostMapping("/{username}")
    public ResponseEntity<JournalEntry> createEntry(@RequestBody JournalEntry myEntry, @PathVariable String username) {
        try{
            journalEntryService.saveEntry(myEntry, username);
            return new ResponseEntity<>(myEntry, HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.NOT_IMPLEMENTED);
        }

    }

    @GetMapping("/{id}")
    public ResponseEntity<JournalEntry> getJournalById(@PathVariable ObjectId id) {
        Optional<JournalEntry> x = journalEntryService.findById(id);
        if(x.isPresent()) return new ResponseEntity<>(x.get(), HttpStatus.OK);
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @DeleteMapping("/{username}/{id}")
    public ResponseEntity<?> deleteJournalById(@PathVariable ObjectId id, @PathVariable String username) {
        journalEntryService.deleteById(id, username);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @PutMapping("/{username}/{id}")
    public ResponseEntity<?> updateJournalById(
            @PathVariable ObjectId id,
            @PathVariable String username,
            @RequestBody JournalEntry myEntry)
    {
        JournalEntry old = journalEntryService.findById(id).orElse(null);
        if(old != null){
            old.setTitle(myEntry.getTitle() != null && !myEntry.getTitle().equals("") ? myEntry.getTitle() : old.getTitle());
            old.setContent(myEntry.getContent() != null && !myEntry.getContent().equals("") ? myEntry.getContent() : old.getContent());
            journalEntryService.saveEntry(old);
            return new ResponseEntity<>(old, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

}
