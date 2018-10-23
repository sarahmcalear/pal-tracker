package io.pivotal.pal.tracker;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.actuate.metrics.CounterService;
import org.springframework.boot.actuate.metrics.GaugeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class TimeEntryController {

    private final CounterService counter;
    private final GaugeService gauge;
    TimeEntryRepository timeEntryRepository;

    public TimeEntryController(
            TimeEntryRepository timeEntryRepository,
            @Qualifier("counterService") CounterService counter,
            @Qualifier("gaugeService") GaugeService gauge
    ) {
        this.timeEntryRepository = timeEntryRepository;
        this.counter = counter;
        this.gauge = gauge;
    }

    @PostMapping("/time-entries")
    public ResponseEntity<TimeEntry> create(
            @RequestBody TimeEntry timeEntryToCreate
    ) {
        TimeEntry timeEntry = timeEntryRepository.create(timeEntryToCreate);
        counter.increment("TimeEntry.created");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());

        return new ResponseEntity<>(timeEntry, HttpStatus.CREATED);
    }

    @GetMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> read(@PathVariable long id) {
        TimeEntry timeEntry = timeEntryRepository.find(id);

        if (timeEntry == null) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        counter.increment("TimeEntry.read");
        return ResponseEntity.ok(timeEntry);
    }

    @GetMapping("/time-entries")
    public ResponseEntity<List<TimeEntry>> list() {
        counter.increment("TimeEntry.listed");
        List<TimeEntry> timeEntries = timeEntryRepository.list();

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

        counter.increment("TimeEntry.updated");
        return ResponseEntity.ok(updatedTimeEntry);
    }

    @DeleteMapping("/time-entries/{id}")
    public ResponseEntity<TimeEntry> delete(@PathVariable long id) {
        timeEntryRepository.delete(id);
        counter.increment("TimeEntry.deleted");
        gauge.submit("timeEntries.count", timeEntryRepository.list().size());

        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
