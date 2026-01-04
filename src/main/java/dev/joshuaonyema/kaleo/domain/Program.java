package dev.joshuaonyema.kaleo.domain;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "programs")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Program {
    @Id
    @Column(name = "Id", updatable = false, nullable = false)
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Column(name = "venue", nullable = false)
    private String venue;

    @Enumerated(EnumType.STRING)
    @Column(name = "program_status", nullable = false)
    private ProgramStatusEnum programStatus;

    @Column(name = "registration_start")
    private LocalDateTime registrationStart;

    @Column(name = "registration_end")
    private LocalDateTime registrationEndTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "organizer_id")
    private User organizer;

    @ManyToMany(mappedBy = "attendingProgram")
    private List<User> participants = new ArrayList<>();

    @ManyToMany(mappedBy = "staffingProgram")
    private  List<User> staff = new ArrayList<>();

    @CreatedDate
    @Column(name = "created_at", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @CreatedDate
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
