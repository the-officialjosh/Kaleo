package dev.joshuaonyema.kaleo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "registration_passes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegistrationPass extends AuditedEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RegistrationPassStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pass_type_id")
    private RegistrationPassType passType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrant_id")
    private User registrant;

    @OneToMany(mappedBy = "pass", cascade = CascadeType.ALL)
    private List<RegistrationPassValidation> passValidations = new ArrayList<>();

    @OneToMany(mappedBy = "pass", cascade = CascadeType.ALL)
    private List<QrCode> qrCodes = new ArrayList<>();

}
