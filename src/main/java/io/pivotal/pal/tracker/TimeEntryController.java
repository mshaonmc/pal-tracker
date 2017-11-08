package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Created by e018538 on 11/8/17.
 */
@RestController
@RequestMapping("/time-entries")
public class TimeEntryController {

    private TimeEntryRepository repository;

    public TimeEntryController(TimeEntryRepository timeEntriesRepo) {
        this.repository = timeEntriesRepo;
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        return new ResponseEntity<>(repository.list(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry entry) {
        TimeEntry anEntry = repository.create(entry);
        return new ResponseEntity(anEntry, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody TimeEntry entry) {
        TimeEntry updatedEntry = repository.update(id, entry);
        if(updatedEntry != null) {
            return new ResponseEntity(updatedEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity read(@PathVariable Long id) {
        TimeEntry entry = repository.find(id);
        if(entry != null) {
            return new ResponseEntity(entry, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id) {
        repository.delete(id);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
