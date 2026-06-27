package com.jayandhan.portfolio.dto;

import com.jayandhan.portfolio.entity.Reminder;
import com.jayandhan.portfolio.enums.ReminderStatus;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

@Data
public class ReminderResponse {
    private Long id;
    private String title;
    private String category;
    private String description;
    private String comments;
    private LocalDate targetDate;
    private LocalDate reminderDate;
    private int daysBefore;
    private String emailSubject;
    private String priority;
    private ReminderStatus status;
    private boolean autoRepeat;
    private Integer repeatMonths;
    private long daysRemaining; // computed: targetDate - today
    private LocalDateTime sentAt;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public static ReminderResponse from(Reminder r) {
        ReminderResponse res = new ReminderResponse();
        res.setId(r.getId());
        res.setTitle(r.getTitle());
        res.setCategory(r.getCategory());
        res.setDescription(r.getDescription());
        res.setComments(r.getComments());
        res.setTargetDate(r.getTargetDate());
        res.setReminderDate(r.getReminderDate());
        res.setDaysBefore(r.getDaysBefore());
        res.setEmailSubject(r.getEmailSubject());
        res.setPriority(r.getPriority());
        res.setStatus(r.getStatus());
        res.setAutoRepeat(r.isAutoRepeat());
        res.setRepeatMonths(r.getRepeatMonths());
        res.setSentAt(r.getSentAt());
        res.setCreatedAt(r.getCreatedAt());
        res.setUpdatedAt(r.getUpdatedAt());
        res.setDaysRemaining(ChronoUnit.DAYS.between(LocalDate.now(), r.getTargetDate()));
        return res;
    }
}
