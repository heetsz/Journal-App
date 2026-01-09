package com.heet.journalApp.controller;

import com.heet.journalApp.entity.JournalEntry;
import com.heet.journalApp.service.JournalEntryService;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping
    public List<JournalEntry> getAll() {
        return journalEntryService.getAll();
    }

    @PostMapping
    public JournalEntry createEntry(@RequestBody JournalEntry myEntry) {
        myEntry.setDate(LocalDateTime.now() );
        journalEntryService.saveEntry(myEntry);
        return myEntry;
    }

    @GetMapping("id/{id}")
    public Optional<JournalEntry> getJournalById(@PathVariable ObjectId id) {
        return journalEntryService.findById(id);
    }

    @DeleteMapping("id/{id}")
    public boolean deleteJournalById(@PathVariable ObjectId id) {
        journalEntryService.deleteById(id);
        return true;
    }

    @PutMapping("id/{id}")
    public JournalEntry updateJournalById(@PathVariable ObjectId id, @RequestBody JournalEntry myEntry) {
        JournalEntry old = journalEntryService.findById(id).orElse(null);
        if(old != null){
            old.setTitle(myEntry.getTitle() != null && !myEntry.getTitle().equals("") ? myEntry.getTitle() : old.getTitle());
            old.setContent(myEntry.getContent() != null && !myEntry.getContent().equals("") ? myEntry.getContent() : old.getContent());
        }
        journalEntryService.saveEntry(old);
        return old;

    }

}
