package dev.joshuaonyema.kaleo.repository;

import dev.joshuaonyema.kaleo.domain.entity.PassType;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface PassTypeRepository extends JpaRepository<PassType, UUID> {
    @Query("SELECT pt FROM PassType pt WHERE pt.id = :id")
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    Optional<PassType> findByIdWithLock(@Param("id") UUID id);
}
