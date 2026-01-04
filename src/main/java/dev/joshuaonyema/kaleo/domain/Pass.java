package dev.joshuaonyema.kaleo.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "registration_passes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Pass extends AuditedEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private PassStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pass_type_id")
    private PassType passType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "registrant_id")
    private User registrant;

    @OneToMany(mappedBy = "pass", cascade = CascadeType.ALL)
    private List<PassValidation> passValidations = new ArrayList<>();

    @OneToMany(mappedBy = "pass", cascade = CascadeType.ALL)
    private List<QrCode> qrCodes = new ArrayList<>();

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Pass that = (Pass) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
