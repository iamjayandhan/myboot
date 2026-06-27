package com.jayandhan.portfolio.service;

import com.jayandhan.portfolio.entity.Reminder;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailService {

    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;

    @Value("${reminder.recipient.email}")
    private String recipientEmail;

    @Value("${spring.mail.username}")
    private String senderEmail;

    public void sendReminderEmail(Reminder reminder) {
        try {
            long daysRemaining = ChronoUnit.DAYS.between(LocalDate.now(), reminder.getTargetDate());

            // Build Thymeleaf context with all template variables
            Context context = new Context();
            context.setVariable("title", reminder.getTitle());
            context.setVariable("category", reminder.getCategory());
            context.setVariable("description", reminder.getDescription());
            context.setVariable("comments", reminder.getComments());
            context.setVariable("targetDate", reminder.getTargetDate());
            context.setVariable("reminderDate", reminder.getReminderDate());
            context.setVariable("priority", reminder.getPriority());
            context.setVariable("daysRemaining", daysRemaining);
            context.setVariable("autoRepeat", reminder.isAutoRepeat());
            context.setVariable("repeatMonths", reminder.getRepeatMonths());

            // Render HTML body from Thymeleaf template
            String htmlBody = templateEngine.process("email/reminder", context);

            // Build the email subject
            String subject = (reminder.getEmailSubject() != null && !reminder.getEmailSubject().isBlank())
                    ? reminder.getEmailSubject()
                    : "⏰ Reminder: " + reminder.getTitle() + " in " + daysRemaining + " days";

            // Send HTML email
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(senderEmail);
            helper.setTo(recipientEmail);
            helper.setSubject(subject);
            helper.setText(htmlBody, true); // true = HTML

            mailSender.send(message);
            log.info("✅ Reminder email sent for: '{}' to {}", reminder.getTitle(), recipientEmail);

        } catch (MessagingException e) {
            log.error("❌ Failed to send reminder email for: '{}'. Error: {}", reminder.getTitle(), e.getMessage());
            throw new RuntimeException("Failed to send email for reminder: " + reminder.getTitle(), e);
        }
    }
}
