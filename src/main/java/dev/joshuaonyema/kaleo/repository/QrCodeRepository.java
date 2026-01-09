package dev.joshuaonyema.kaleo.repository;

import dev.joshuaonyema.kaleo.domain.entity.QrCode;
import dev.joshuaonyema.kaleo.domain.entity.QrCodeStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {
    Optional<QrCode> findByPassIdAndPassRegistrantId(UUID passId, UUID registrantID);
    Optional<QrCode> findByIdAndStatus(UUID id, QrCodeStatus status);
}
