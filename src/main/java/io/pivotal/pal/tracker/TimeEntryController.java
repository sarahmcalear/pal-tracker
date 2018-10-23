package io.pivotal.pal.tracker;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {
    TimeEntryRepository timeEntryRepository;

    public TimeEntryController(TimeEntryRepository timeEntryRepository) {
        this.timeEntryRepository = timeEntryRepository;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(
            @RequestBody TimeEntry timeEntryToCreate
    ) {
        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);
        return new ResponseEntity<>(timeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry timeEntry = timeEntryRepository.find(id);

        if (timeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(timeEntry);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        List<TimeEntry> timeEntries = timeEntryRepository.list();

        if (timeEntries.size() == 0) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(timeEntries);
    }

    @PutMapping("/time-entries/{id}")
    public ResponseEntity update(
            @PathVariable long id,
            @RequestBody TimeEntry timeEntryToUpdate
    ) {
        TimeEntry updatedTimeEntry = timeEntryRepository.update(id, timeEntryToUpdate);

        if (updatedTimeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(updatedTimeEntry);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        timeEntryRepository.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
