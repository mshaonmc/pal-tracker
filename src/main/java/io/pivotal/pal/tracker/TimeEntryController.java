package io.pivotal.pal.tracker;

import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
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
    private CounterService counter;
    private GaugeService gauge;

    public TimeEntryController(TimeEntryRepository timeEntriesRepo, CounterService counterService, GaugeService gaugeService) {
        this.repository = timeEntriesRepo;
        this.counter = counterService;
        this.gauge = gaugeService;
    }

    @GetMapping
    public ResponseEntity<List<TimeEntry>> list() {
        counter.increment("TimeEntry.listed");
        return new ResponseEntity<>(repository.list(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity create(@RequestBody TimeEntry entry) {
        TimeEntry anEntry = repository.create(entry);
        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", repository.list().size());

        return new ResponseEntity(anEntry, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity update(@PathVariable Long id, @RequestBody TimeEntry entry) {
        TimeEntry updatedEntry = repository.update(id, entry);
        if(updatedEntry != null) {
            counter.increment("TimeEntry.updated");
            return new ResponseEntity(updatedEntry, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("{id}")
    public ResponseEntity read(@PathVariable Long id) {
        TimeEntry entry = repository.find(id);
        if(entry != null) {
            counter.increment("TimeEntry.read");
            return new ResponseEntity(entry, HttpStatus.OK);
        } else {
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable Long id) {
        repository.delete(id);
        counter.increment("TimeEntry.deleted");
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
