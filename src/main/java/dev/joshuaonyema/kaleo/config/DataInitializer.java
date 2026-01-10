package dev.joshuaonyema.kaleo.config;

import dev.joshuaonyema.kaleo.domain.entity.PassType;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import dev.joshuaonyema.kaleo.domain.entity.User;
import dev.joshuaonyema.kaleo.repository.ProgramRepository;
import dev.joshuaonyema.kaleo.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Initializes demo data for the application on startup.
 * Only runs in the "dev" profile to avoid polluting production databases.
 */
@Component
@Profile("dev")
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final ProgramRepository programRepository;
    private final UserRepository userRepository;

    // Keycloak user IDs from realm-export.json
    private static final UUID ORGANIZER_ID = UUID.fromString("f47ac10b-58cc-4372-a567-0e02b2c3d479");
    private static final UUID STAFF_ID = UUID.fromString("6ba7b810-9dad-11d1-80b4-00c04fd430c8");
    private static final UUID ATTENDEE_ID = UUID.fromString("550e8400-e29b-41d4-a716-446655440000");

    @Override
    @Transactional
    public void run(String... args) {
        if (programRepository.count() > 0) {
            log.info("Demo data already exists, skipping initialization");
            return;
        }

        log.info("Initializing demo data...");
        
        // Ensure organizer user exists
        User organizer = userRepository.findById(ORGANIZER_ID)
                .orElseGet(() -> {
                    User newOrganizer = new User();
                    newOrganizer.setId(ORGANIZER_ID);
                    newOrganizer.setName("Event Organizer");
                    newOrganizer.setEmail("organizer@kaleo.dev");
                    return userRepository.save(newOrganizer);
                });

        LocalDateTime now = LocalDateTime.now();

        // Create 10 church-themed programs
        createProgram(organizer, "Sunday Worship Experience",
                "Grace Community Church, 1234 Faith Avenue, Toronto, ON M5V 2T6",
                now.plusDays(3).withHour(9).withMinute(0),
                now.plusDays(3).withHour(12).withMinute(30),
                now.minusDays(7), now.plusDays(2),
                ProgramStatus.PUBLISHED,
                List.of(
                        new PassTypeData("Free Entry", BigDecimal.ZERO, "Open to all members of the community", 500),
                        new PassTypeData("Reserved Seating", new BigDecimal("15.00"), "Premium seating near the front with program booklet", 100),
                        new PassTypeData("VIP Family Package", new BigDecimal("50.00"), "Reserved family seating for up to 6 people with parking", 25)
                ));

        createProgram(organizer, "Youth Fire Conference 2026",
                "Montreal Convention Center, 500 Viger Street, Montreal, QC H2Z 1G1",
                now.plusDays(14).withHour(18).withMinute(0),
                now.plusDays(16).withHour(21).withMinute(0),
                now.minusDays(14), now.plusDays(10),
                ProgramStatus.PUBLISHED,
                List.of(
                        new PassTypeData("Early Bird", new BigDecimal("25.00"), "Limited early registration discount", 200),
                        new PassTypeData("Standard Pass", new BigDecimal("45.00"), "Full conference access for all 3 days", 500),
                        new PassTypeData("VIP Experience", new BigDecimal("120.00"), "Front row seating, meet & greet with speakers, exclusive merchandise", 50),
                        new PassTypeData("Group of 10", new BigDecimal("350.00"), "Discounted rate for youth group leaders bringing 10+ members", 30)
                ));

        createProgram(organizer, "Marriage Enrichment Retreat",
                "Muskoka Lakeside Resort, 789 Retreat Road, Muskoka, ON P1L 1H7",
                now.plusDays(30).withHour(14).withMinute(0),
                now.plusDays(32).withHour(12).withMinute(0),
                now.minusDays(5), now.plusDays(25),
                ProgramStatus.PUBLISHED,
                List.of(
                        new PassTypeData("Couple's Standard", new BigDecimal("299.00"), "Shared accommodation, all meals included, workshop access", 40),
                        new PassTypeData("Couple's Suite", new BigDecimal("449.00"), "Private suite with lake view, all meals, spa session", 15),
                        new PassTypeData("Day Pass (Saturday)", new BigDecimal("75.00"), "Attend Saturday workshops only, lunch included", 30)
                ));

        createProgram(organizer, "Women's Prayer Breakfast",
                "Hilton Garden Inn, 2100 Lake Shore Boulevard, Toronto, ON M8V 4B1",
                now.plusDays(7).withHour(7).withMinute(30),
                now.plusDays(7).withHour(11).withMinute(0),
                now.minusDays(10), now.plusDays(5),
                ProgramStatus.PUBLISHED,
                List.of(
                        new PassTypeData("General Admission", new BigDecimal("35.00"), "Full breakfast buffet and program", 150),
                        new PassTypeData("Table Host", new BigDecimal("280.00"), "Host a table of 8, includes reserved seating and recognition", 20),
                        new PassTypeData("Student Rate", new BigDecimal("15.00"), "Discounted rate for students with valid ID", 50)
                ));

        createProgram(organizer, "Easter Celebration Service",
                "BMO Field Outdoor Venue, 170 Princes' Boulevard, Toronto, ON M6K 3C3",
                now.plusDays(60).withHour(6).withMinute(0),
                now.plusDays(60).withHour(10).withMinute(0),
                now.plusDays(1), now.plusDays(55),
                ProgramStatus.PUBLISHED,
                List.of(
                        new PassTypeData("Free Admission", BigDecimal.ZERO, "General lawn seating, bring your own chair", 5000),
                        new PassTypeData("Reserved Seating", new BigDecimal("10.00"), "Stadium seating with program booklet", 2000),
                        new PassTypeData("VIP Sunrise Experience", new BigDecimal("75.00"), "Premium seating, breakfast after service, commemorative gift", 200)
                ));

        createProgram(organizer, "Men's Leadership Summit",
                "Kingdom Life Church, 456 Victory Lane, Vancouver, BC V6B 2W9",
                now.plusDays(21).withHour(9).withMinute(0),
                now.plusDays(21).withHour(17).withMinute(0),
                now.minusDays(3), now.plusDays(18),
                ProgramStatus.PUBLISHED,
                List.of(
                        new PassTypeData("Early Registration", new BigDecimal("40.00"), "Includes lunch, workshop materials, and networking session", 75),
                        new PassTypeData("Standard Registration", new BigDecimal("55.00"), "Same benefits, regular pricing", 150),
                        new PassTypeData("Pastor's Track", new BigDecimal("85.00"), "Special leadership breakout sessions for ministry leaders", 40)
                ));

        createProgram(organizer, "Christmas Carol Concert",
                "Roy Thomson Hall, 60 Simcoe Street, Toronto, ON M5J 2H5",
                now.plusDays(45).withHour(19).withMinute(0),
                now.plusDays(45).withHour(22).withMinute(0),
                now.plusDays(5), now.plusDays(40),
                ProgramStatus.DRAFT,
                List.of(
                        new PassTypeData("Balcony", new BigDecimal("25.00"), "Upper balcony seating", 500),
                        new PassTypeData("Orchestra", new BigDecimal("45.00"), "Main floor seating", 800),
                        new PassTypeData("Premium Orchestra", new BigDecimal("75.00"), "Front section with meet and greet", 150),
                        new PassTypeData("Family Pack (4)", new BigDecimal("120.00"), "4 orchestra seats at discounted rate", 100)
                ));

        createProgram(organizer, "Worship Night Live",
                "Massey Hall, 178 Victoria Street, Toronto, ON M5B 1T7",
                now.plusDays(10).withHour(19).withMinute(30),
                now.plusDays(10).withHour(22).withMinute(0),
                now.minusDays(21), now.plusDays(8),
                ProgramStatus.PUBLISHED,
                List.of(
                        new PassTypeData("General Admission", new BigDecimal("20.00"), "Standing room and balcony access", 1000),
                        new PassTypeData("Reserved Floor", new BigDecimal("40.00"), "Guaranteed floor section seating", 400),
                        new PassTypeData("VIP Meet & Worship", new BigDecimal("100.00"), "Soundcheck access, photo opportunity, premium seating", 75)
                ));

        createProgram(organizer, "Children's VBS: Kingdom Adventures",
                "New Life Community Center, 321 Hope Street, Mississauga, ON L5B 3C1",
                now.minusDays(30).withHour(9).withMinute(0),
                now.minusDays(26).withHour(15).withMinute(0),
                now.minusDays(60), now.minusDays(32),
                ProgramStatus.COMPLETED,
                List.of(
                        new PassTypeData("Child Registration", new BigDecimal("25.00"), "Full week VBS experience ages 4-12, snacks included", 150),
                        new PassTypeData("Sibling Discount", new BigDecimal("20.00"), "Reduced rate for additional children from same family", 75),
                        new PassTypeData("Teen Volunteer", BigDecimal.ZERO, "Ages 13-17 serving as junior counselors", 30)
                ));

        createProgram(organizer, "Global Missions Gala",
                "The Carlu, 444 Yonge Street, Toronto, ON M5B 2H4",
                now.plusDays(45).withHour(18).withMinute(0),
                now.plusDays(45).withHour(22).withMinute(30),
                now.plusDays(10), now.plusDays(40),
                ProgramStatus.DRAFT,
                List.of(
                        new PassTypeData("Individual Seat", new BigDecimal("150.00"), "Three-course dinner, program, and silent auction access", 200),
                        new PassTypeData("Table of 8", new BigDecimal("1000.00"), "Reserved table with recognition in program booklet", 25),
                        new PassTypeData("Platinum Sponsor Table", new BigDecimal("2500.00"), "Premium table, logo placement, VIP reception access", 10)
                ));

        log.info("Demo data initialization complete! Created 10 church programs with pass types.");
    }

    private void createProgram(User organizer, String name, String venue,
                                LocalDateTime startTime, LocalDateTime endTime,
                                LocalDateTime registrationStart, LocalDateTime registrationEnd,
                                ProgramStatus status, List<PassTypeData> passTypesData) {
        Program program = new Program();
        program.setName(name);
        program.setVenue(venue);
        program.setStartTime(startTime);
        program.setEndTime(endTime);
        program.setRegistrationStart(registrationStart);
        program.setRegistrationEnd(registrationEnd);
        program.setStatus(status);
        program.setOrganizer(organizer);

        for (PassTypeData ptd : passTypesData) {
            PassType passType = new PassType();
            passType.setName(ptd.name());
            passType.setPrice(ptd.price());
            passType.setDescription(ptd.description());
            passType.setTotalAvailable(ptd.totalAvailable());
            passType.setProgram(program);
            program.getPassTypes().add(passType);
        }

        programRepository.save(program);
        log.debug("Created program: {}", name);
    }

    private record PassTypeData(String name, BigDecimal price, String description, Integer totalAvailable) {}
}
