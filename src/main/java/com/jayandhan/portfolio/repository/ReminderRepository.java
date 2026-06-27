package com.jayandhan.portfolio.repository;

import com.jayandhan.portfolio.entity.Reminder;
import com.jayandhan.portfolio.enums.ReminderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ReminderRepository extends JpaRepository<Reminder, Long> {

    // Used by the daily scheduler — find reminders due today
    List<Reminder> findByReminderDateAndStatus(LocalDate reminderDate, ReminderStatus status);

    // List all pending reminders ordered by date
    List<Reminder> findByStatusOrderByReminderDateAsc(ReminderStatus status);

    // Upcoming reminders within the next N days
    List<Reminder> findByReminderDateBetweenAndStatusOrderByReminderDateAsc(
            LocalDate from, LocalDate to, ReminderStatus status);

    // Filter by category
    List<Reminder> findByCategoryIgnoreCaseOrderByReminderDateAsc(String category);
}
