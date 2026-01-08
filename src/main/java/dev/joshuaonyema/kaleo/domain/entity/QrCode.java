package dev.joshuaonyema.kaleo.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;
import java.util.UUID;

@Entity
@Table(name = "qr_codes")
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class QrCode extends TimestampedEntity {
    @Id
    @Column(name = "id", updatable = false, nullable = false)
    private UUID id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private QrCodeStatus status;

    @Column(name = "value", columnDefinition = "TEXT", nullable = false)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pass_id")
    private Pass pass;

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        QrCode qrCode = (QrCode) o;
        return Objects.equals(id, qrCode.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
