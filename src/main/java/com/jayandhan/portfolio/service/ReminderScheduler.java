package com.jayandhan.portfolio.service;

import com.jayandhan.portfolio.entity.Reminder;
import com.jayandhan.portfolio.enums.ReminderStatus;
import com.jayandhan.portfolio.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReminderScheduler {

    private final ReminderRepository reminderRepository;
    private final EmailService emailService;
    private final ReminderService reminderService;

    /**
     * Runs every day at 8:00 AM IST.
     * Finds all PENDING reminders where reminderDate = today, sends email, marks as SENT.
     */
    @Scheduled(cron = "0 0 8 * * *", zone = "Asia/Kolkata")
    public void processDailyReminders() {
        LocalDate today = LocalDate.now();
        log.info("📅 Scheduler triggered on {} — checking for due reminders...", today);

        List<Reminder> due = reminderRepository.findByReminderDateAndStatus(today, ReminderStatus.PENDING);

        if (due.isEmpty()) {
            log.info("✅ No reminders due today.");
            return;
        }

        log.info("📬 Found {} reminder(s) due today. Sending emails...", due.size());

        for (Reminder reminder : due) {
            try {
                emailService.sendReminderEmail(reminder);

                // Mark as SENT
                reminder.setStatus(ReminderStatus.SENT);
                reminder.setSentAt(java.time.LocalDateTime.now());
                reminderRepository.save(reminder);

                log.info("✅ Marked reminder '{}' (id={}) as SENT.", reminder.getTitle(), reminder.getId());

                // Handle auto-repeat (e.g., PUCC every 6 months)
                reminderService.processAutoRepeat(reminder);

            } catch (Exception e) {
                log.error("❌ Error processing reminder '{}' (id={}): {}",
                        reminder.getTitle(), reminder.getId(), e.getMessage());
            }
        }

        log.info("📬 Daily reminder processing complete. {} processed.", due.size());
    }
}
