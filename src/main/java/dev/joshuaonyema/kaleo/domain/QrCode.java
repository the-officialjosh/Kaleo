package dev.joshuaonyema.kaleo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "qr_codes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class QrCode extends AuditedEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QrCodeStatus status;

    @Column(name = "value", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pass_id")
    private RegistrationPass pass;

}
