package dev.joshuaonyema.kaleo.repository;

import dev.joshuaonyema.kaleo.domain.entity.PassType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PassTypeRepository extends JpaRepository<PassType, UUID> {
}
