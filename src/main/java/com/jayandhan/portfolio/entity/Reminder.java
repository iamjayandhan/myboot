package com.jayandhan.portfolio.entity;

import com.jayandhan.portfolio.enums.ReminderStatus;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "reminders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reminder {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false, length = 50)
    private String category; // e.g. "vehicle", "personal", "family", "finance"

    @Column(columnDefinition = "TEXT")
    private String description; // Email body content

    @Column(columnDefinition = "TEXT")
    private String comments; // Personal notes

    @Column(name = "target_date", nullable = false)
    private LocalDate targetDate; // Actual expiry/due date

    @Column(name = "reminder_date", nullable = false)
    private LocalDate reminderDate; // Auto: targetDate - daysBefore

    @Column(name = "days_before", nullable = false)
    @Builder.Default
    private int daysBefore = 15;

    @Column(name = "email_subject")
    private String emailSubject; // Custom subject for the email

    @Column(nullable = false, length = 10)
    @Builder.Default
    private String priority = "MEDIUM"; // LOW, MEDIUM, HIGH

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    @Builder.Default
    private ReminderStatus status = ReminderStatus.PENDING;

    @Column(name = "auto_repeat", nullable = false)
    @Builder.Default
    private boolean autoRepeat = false;

    @Column(name = "repeat_months")
    private Integer repeatMonths; // Gap in months if auto_repeat is true

    @Column(name = "sent_at")
    private LocalDateTime sentAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
        // Auto-compute reminderDate if not set
        if (reminderDate == null && targetDate != null) {
            reminderDate = targetDate.minusDays(daysBefore);
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
