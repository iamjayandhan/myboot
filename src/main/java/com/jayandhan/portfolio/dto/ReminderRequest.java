package com.jayandhan.portfolio.dto;

import lombok.Data;
import java.time.LocalDate;

@Data
public class ReminderRequest {
    private String title;
    private String category;
    private String description;
    private String comments;
    private LocalDate targetDate;
    private LocalDate reminderDate; // Optional — if null, auto-computed as targetDate - daysBefore
    private Integer daysBefore;     // Optional — defaults to 15
    private String emailSubject;
    private String priority;        // LOW, MEDIUM, HIGH — defaults to MEDIUM
    private boolean autoRepeat;
    private Integer repeatMonths;
}
