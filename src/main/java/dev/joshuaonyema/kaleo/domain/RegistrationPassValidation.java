package dev.joshuaonyema.kaleo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "registration_pass_validations")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class RegistrationPassValidation extends AuditedEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private RegistrationPassValidationStatus passStatus;

    @Enumerated(EnumType.STRING)
    @Column(name = "validation_method", nullable = false)
    private RegistrationPassValidationMethod validationMethod;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pass_id")
    private RegistrationPass pass;

}
