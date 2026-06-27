package com.jayandhan.portfolio.service;

import com.jayandhan.portfolio.entity.Reminder;
import com.jayandhan.portfolio.enums.ReminderStatus;
import com.jayandhan.portfolio.repository.ReminderRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataInitializerService {

    private final ReminderRepository reminderRepository;

    @EventListener(ApplicationReadyEvent.class)
    public void seedData() {
        if (reminderRepository.count() > 0) {
            log.info("⏭️  Database already has data — skipping seed.");
            return;
        }

        log.info("🌱 Seeding initial vehicle reminder data...");

        List<Reminder> reminders = List.of(

            // ─── Honda Activa 125 | TN66AV1311 ─────────────────────────────

            Reminder.builder()
                .title("Honda Activa 125 — PUCC Renewal")
                .category("vehicle")
                .description("Pollution Under Control Certificate (PUCC) for Honda Activa 125 (TN66AV1311) is expiring. Renew at any authorized emission test center near you.")
                .comments("TN66AV1311 | RTO: Coimbatore Central | Renews every 6 months after this.")
                .targetDate(LocalDate.of(2026, 8, 13))
                .reminderDate(LocalDate.of(2026, 7, 29))
                .daysBefore(15)
                .emailSubject("⚠️ Activa PUCC Expiring in 15 Days — TN66AV1311")
                .priority("HIGH")
                .status(ReminderStatus.PENDING)
                .autoRepeat(true)
                .repeatMonths(6)
                .build(),

            Reminder.builder()
                .title("Honda Activa 125 — Standard Warranty Expiry")
                .category("vehicle")
                .description("Standard Warranty for Honda Activa 125 (TN66AV1311) is expiring. Consider purchasing Extended Warranty before it lapses.")
                .comments("TN66AV1311 | Valid from: 07-Aug-2025 | Valid till: 06-Aug-2028")
                .targetDate(LocalDate.of(2028, 8, 6))
                .reminderDate(LocalDate.of(2028, 7, 22))
                .daysBefore(15)
                .emailSubject("⚠️ Activa Standard Warranty Ending — TN66AV1311")
                .priority("MEDIUM")
                .status(ReminderStatus.PENDING)
                .autoRepeat(false)
                .build(),

            Reminder.builder()
                .title("Honda Activa 125 — Insurance Renewal")
                .category("vehicle")
                .description("Vehicle Insurance for Honda Activa 125 (TN66AV1311) is due for renewal. Ensure you renew before expiry to avoid driving uninsured.")
                .comments("TN66AV1311 | Valid till: 06-Aug-2030")
                .targetDate(LocalDate.of(2030, 8, 6))
                .reminderDate(LocalDate.of(2030, 7, 22))
                .daysBefore(15)
                .emailSubject("⚠️ Activa Insurance Expiring — TN66AV1311")
                .priority("HIGH")
                .status(ReminderStatus.PENDING)
                .autoRepeat(false)
                .build(),

            Reminder.builder()
                .title("Honda Activa 125 — Extended Warranty Expiry")
                .category("vehicle")
                .description("Extended Warranty for Honda Activa 125 (TN66AV1311) is expiring.")
                .comments("TN66AV1311 | Valid from: 07-Aug-2028 | Valid till: 07-Aug-2031")
                .targetDate(LocalDate.of(2031, 8, 7))
                .reminderDate(LocalDate.of(2031, 7, 23))
                .daysBefore(15)
                .emailSubject("⚠️ Activa Extended Warranty Ending — TN66AV1311")
                .priority("MEDIUM")
                .status(ReminderStatus.PENDING)
                .autoRepeat(false)
                .build(),

            Reminder.builder()
                .title("Honda Activa 125 — Fitness Certificate Expiry")
                .category("vehicle")
                .description("Fitness Certificate for Honda Activa 125 (TN66AV1311) is expiring. Renew via your local RTO.")
                .comments("TN66AV1311 | Valid till: 13-Aug-2040")
                .targetDate(LocalDate.of(2040, 8, 13))
                .reminderDate(LocalDate.of(2040, 7, 29))
                .daysBefore(15)
                .emailSubject("⚠️ Activa Fitness Certificate Expiring — TN66AV1311")
                .priority("HIGH")
                .status(ReminderStatus.PENDING)
                .autoRepeat(false)
                .build(),

            // ─── Maruti Suzuki Wagon R ZXI+ | TN38DT7594 ───────────────────

            Reminder.builder()
                .title("Wagon R — PUCC Renewal")
                .category("vehicle")
                .description("Pollution Under Control Certificate (PUCC) for Maruti Suzuki Wagon R ZXI+ (TN38DT7594) is expiring. Renew at any authorized emission test center.")
                .comments("TN38DT7594 | RTO: Coimbatore North | Renews every 6 months.")
                .targetDate(LocalDate.of(2027, 3, 23))
                .reminderDate(LocalDate.of(2027, 3, 8))
                .daysBefore(15)
                .emailSubject("⚠️ Wagon R PUCC Expiring in 15 Days — TN38DT7594")
                .priority("HIGH")
                .status(ReminderStatus.PENDING)
                .autoRepeat(true)
                .repeatMonths(6)
                .build(),

            Reminder.builder()
                .title("Wagon R — Insurance Renewal")
                .category("vehicle")
                .description("Vehicle Insurance (IFFCO Tokio General Insurance — Policy: MR00380142) for Maruti Suzuki Wagon R ZXI+ (TN38DT7594) is due for renewal. Comprehensive + Zero Dep + DEPT + EP + RTI + RSA cover.")
                .comments("TN38DT7594 | Policy: MR00380142 | IFFCO Tokio | Valid till: 22-Mar-2029")
                .targetDate(LocalDate.of(2029, 3, 22))
                .reminderDate(LocalDate.of(2029, 3, 7))
                .daysBefore(15)
                .emailSubject("⚠️ Wagon R Insurance Expiring — TN38DT7594")
                .priority("HIGH")
                .status(ReminderStatus.PENDING)
                .autoRepeat(false)
                .build(),

            Reminder.builder()
                .title("Wagon R — Standard Warranty Expiry")
                .category("vehicle")
                .description("Standard Warranty for Maruti Suzuki Wagon R ZXI+ (TN38DT7594) is expiring (whichever comes first: date or 1,00,000 km).")
                .comments("TN38DT7594 | Valid till: 16-Mar-2029 or 1,00,000 km")
                .targetDate(LocalDate.of(2029, 3, 16))
                .reminderDate(LocalDate.of(2029, 3, 1))
                .daysBefore(15)
                .emailSubject("⚠️ Wagon R Standard Warranty Ending — TN38DT7594")
                .priority("MEDIUM")
                .status(ReminderStatus.PENDING)
                .autoRepeat(false)
                .build(),

            Reminder.builder()
                .title("Wagon R — CCP Package Expiry")
                .category("vehicle")
                .description("Customer Convenience Package (Royal Platinum Hydro & Rodent Protect 3 Year) for Maruti Suzuki Wagon R ZXI+ (TN38DT7594) is expiring.")
                .comments("TN38DT7594 | Valid till: 22-Mar-2029 or 1,00,000 km")
                .targetDate(LocalDate.of(2029, 3, 22))
                .reminderDate(LocalDate.of(2029, 3, 7))
                .daysBefore(15)
                .emailSubject("⚠️ Wagon R CCP Package Expiring — TN38DT7594")
                .priority("MEDIUM")
                .status(ReminderStatus.PENDING)
                .autoRepeat(false)
                .build(),

            Reminder.builder()
                .title("Wagon R — Extended Warranty (Solitaire) Expiry")
                .category("vehicle")
                .description("Extended Warranty (Solitaire) for Maruti Suzuki Wagon R ZXI+ (TN38DT7594) is expiring (whichever comes first: date or 1,60,000 km).")
                .comments("TN38DT7594 | Valid till: 15-Mar-2032 or 1,60,000 km")
                .targetDate(LocalDate.of(2032, 3, 15))
                .reminderDate(LocalDate.of(2032, 2, 29))
                .daysBefore(15)
                .emailSubject("⚠️ Wagon R Extended Warranty Ending — TN38DT7594")
                .priority("MEDIUM")
                .status(ReminderStatus.PENDING)
                .autoRepeat(false)
                .build(),

            Reminder.builder()
                .title("Wagon R — Fitness Certificate Expiry")
                .category("vehicle")
                .description("Fitness Certificate for Maruti Suzuki Wagon R ZXI+ (TN38DT7594) is expiring. Renew via your local RTO.")
                .comments("TN38DT7594 | Valid till: 23-Mar-2041 (15 years from registration)")
                .targetDate(LocalDate.of(2041, 3, 23))
                .reminderDate(LocalDate.of(2041, 3, 8))
                .daysBefore(15)
                .emailSubject("⚠️ Wagon R Fitness Certificate Expiring — TN38DT7594")
                .priority("HIGH")
                .status(ReminderStatus.PENDING)
                .autoRepeat(false)
                .build()
        );

        reminderRepository.saveAll(reminders);
        log.info("✅ Seeded {} vehicle reminders successfully.", reminders.size());
    }
}
