package dev.joshuaonyema.kaleo.repository;

import dev.joshuaonyema.kaleo.domain.entity.Pass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PassRepository extends JpaRepository<Pass, UUID> {
    int countByPassTypeId(UUID ticketTypeId);
    Page<Pass> findByRegistrantId(UUID RegistrantId, Pageable pageable);
    Optional<Pass> findByIdAndRegistrantId(UUID id,  UUID Registrant);
}
