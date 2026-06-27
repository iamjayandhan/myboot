package com.jayandhan.portfolio.controller;

import com.jayandhan.portfolio.dto.ReminderRequest;
import com.jayandhan.portfolio.dto.ReminderResponse;
import com.jayandhan.portfolio.service.ReminderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/reminders")
@RequiredArgsConstructor
public class ReminderController {

    private final ReminderService reminderService;

    // GET /api/reminders — list all
    @GetMapping
    public ResponseEntity<List<ReminderResponse>> getAll() {
        return ResponseEntity.ok(reminderService.getAll());
    }

    // GET /api/reminders/upcoming?days=30 — upcoming in next N days (default 30)
    @GetMapping("/upcoming")
    public ResponseEntity<List<ReminderResponse>> getUpcoming(
            @RequestParam(defaultValue = "30") int days) {
        return ResponseEntity.ok(reminderService.getUpcoming(days));
    }

    // GET /api/reminders/category/{category}
    @GetMapping("/category/{category}")
    public ResponseEntity<List<ReminderResponse>> getByCategory(@PathVariable String category) {
        return ResponseEntity.ok(reminderService.getByCategory(category));
    }

    // GET /api/reminders/{id}
    @GetMapping("/{id}")
    public ResponseEntity<ReminderResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(reminderService.getById(id));
    }

    // POST /api/reminders — create
    @PostMapping
    public ResponseEntity<ReminderResponse> create(@RequestBody ReminderRequest request) {
        return ResponseEntity.ok(reminderService.create(request));
    }

    // PUT /api/reminders/{id} — update
    @PutMapping("/{id}")
    public ResponseEntity<ReminderResponse> update(
            @PathVariable Long id, @RequestBody ReminderRequest request) {
        return ResponseEntity.ok(reminderService.update(id, request));
    }

    // DELETE /api/reminders/{id}
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, String>> delete(@PathVariable Long id) {
        reminderService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Reminder deleted successfully."));
    }

    // POST /api/reminders/{id}/trigger — manually fire email now (test)
    @PostMapping("/{id}/trigger")
    public ResponseEntity<Map<String, String>> trigger(@PathVariable Long id) {
        String result = reminderService.triggerEmail(id);
        return ResponseEntity.ok(Map.of("message", result));
    }
}
