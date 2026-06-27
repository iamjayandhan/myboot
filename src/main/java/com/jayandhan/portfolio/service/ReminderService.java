package com.jayandhan.portfolio.service;

import com.jayandhan.portfolio.dto.ReminderRequest;
import com.jayandhan.portfolio.dto.ReminderResponse;
import com.jayandhan.portfolio.entity.Reminder;
import com.jayandhan.portfolio.enums.ReminderStatus;
import com.jayandhan.portfolio.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderService {

    private final ReminderRepository reminderRepository;
    private final EmailService emailService;

    // ─── CREATE ───────────────────────────────────────────────────────────────

    public ReminderResponse create(ReminderRequest req) {
        int daysBefore = (req.getDaysBefore() != null) ? req.getDaysBefore() : 15;
        String priority = (req.getPriority() != null && !req.getPriority().isBlank()) ? req.getPriority() : "MEDIUM";

        // Compute reminder date: if caller provides one, use it; else auto-compute
        LocalDate reminderDate = (req.getReminderDate() != null)
                ? req.getReminderDate()
                : req.getTargetDate().minusDays(daysBefore);

        Reminder reminder = Reminder.builder()
                .title(req.getTitle())
                .category(req.getCategory().toLowerCase())
                .description(req.getDescription())
                .comments(req.getComments())
                .targetDate(req.getTargetDate())
                .reminderDate(reminderDate)
                .daysBefore(daysBefore)
                .emailSubject(req.getEmailSubject())
                .priority(priority.toUpperCase())
                .status(ReminderStatus.PENDING)
                .autoRepeat(req.isAutoRepeat())
                .repeatMonths(req.getRepeatMonths())
                .build();

        return ReminderResponse.from(reminderRepository.save(reminder));
    }

    // ─── READ ─────────────────────────────────────────────────────────────────

    public List<ReminderResponse> getAll() {
        return reminderRepository.findAll()
                .stream().map(ReminderResponse::from).collect(Collectors.toList());
    }

    public ReminderResponse getById(Long id) {
        return ReminderResponse.from(findOrThrow(id));
    }

    public List<ReminderResponse> getUpcoming(int days) {
        LocalDate from = LocalDate.now();
        LocalDate to = from.plusDays(days);
        return reminderRepository
                .findByReminderDateBetweenAndStatusOrderByReminderDateAsc(from, to, ReminderStatus.PENDING)
                .stream().map(ReminderResponse::from).collect(Collectors.toList());
    }

    public List<ReminderResponse> getByCategory(String category) {
        return reminderRepository.findByCategoryIgnoreCaseOrderByReminderDateAsc(category)
                .stream().map(ReminderResponse::from).collect(Collectors.toList());
    }

    // ─── UPDATE ───────────────────────────────────────────────────────────────

    public ReminderResponse update(Long id, ReminderRequest req) {
        Reminder reminder = findOrThrow(id);

        if (req.getTitle() != null) reminder.setTitle(req.getTitle());
        if (req.getCategory() != null) reminder.setCategory(req.getCategory().toLowerCase());
        if (req.getDescription() != null) reminder.setDescription(req.getDescription());
        if (req.getComments() != null) reminder.setComments(req.getComments());
        if (req.getPriority() != null) reminder.setPriority(req.getPriority().toUpperCase());
        if (req.getEmailSubject() != null) reminder.setEmailSubject(req.getEmailSubject());
        if (req.isAutoRepeat()) reminder.setAutoRepeat(true);
        if (req.getRepeatMonths() != null) reminder.setRepeatMonths(req.getRepeatMonths());

        // Update dates — recalculate reminderDate if daysBefore or targetDate changes
        if (req.getTargetDate() != null) reminder.setTargetDate(req.getTargetDate());
        if (req.getDaysBefore() != null) reminder.setDaysBefore(req.getDaysBefore());

        // If manual reminderDate override provided, use it; else recalculate
        if (req.getReminderDate() != null) {
            reminder.setReminderDate(req.getReminderDate());
        } else if (req.getTargetDate() != null || req.getDaysBefore() != null) {
            reminder.setReminderDate(reminder.getTargetDate().minusDays(reminder.getDaysBefore()));
        }

        return ReminderResponse.from(reminderRepository.save(reminder));
    }

    // ─── DELETE ───────────────────────────────────────────────────────────────

    public void delete(Long id) {
        reminderRepository.delete(findOrThrow(id));
        log.info("Deleted reminder id={}", id);
    }

    // ─── MANUAL TRIGGER ───────────────────────────────────────────────────────

    public String triggerEmail(Long id) {
        Reminder reminder = findOrThrow(id);
        emailService.sendReminderEmail(reminder);
        return "Email triggered for: " + reminder.getTitle();
    }

    // ─── AUTO REPEAT (called by scheduler after sending) ─────────────────────

    public void processAutoRepeat(Reminder reminder) {
        if (reminder.isAutoRepeat() && reminder.getRepeatMonths() != null) {
            LocalDate newTarget = reminder.getTargetDate().plusMonths(reminder.getRepeatMonths());
            LocalDate newReminder = newTarget.minusDays(reminder.getDaysBefore());

            Reminder next = Reminder.builder()
                    .title(reminder.getTitle())
                    .category(reminder.getCategory())
                    .description(reminder.getDescription())
                    .comments(reminder.getComments())
                    .targetDate(newTarget)
                    .reminderDate(newReminder)
                    .daysBefore(reminder.getDaysBefore())
                    .emailSubject(reminder.getEmailSubject())
                    .priority(reminder.getPriority())
                    .status(ReminderStatus.PENDING)
                    .autoRepeat(true)
                    .repeatMonths(reminder.getRepeatMonths())
                    .build();

            reminderRepository.save(next);
            log.info("🔁 Auto-repeat: created next reminder '{}' for target date {}", next.getTitle(), newTarget);
        }
    }

    // ─── HELPER ───────────────────────────────────────────────────────────────

    private Reminder findOrThrow(Long id) {
        return reminderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reminder not found with id: " + id));
    }
}
