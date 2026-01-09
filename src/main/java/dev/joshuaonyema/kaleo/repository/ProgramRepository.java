package dev.joshuaonyema.kaleo.repository;

import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProgramRepository extends JpaRepository<Program, UUID> {
    Page<Program> findByOrganizerId(UUID organizerId, Pageable pageable);
    Optional<Program> findByIdAndOrganizerId(UUID id, UUID organizerId);
    Page<Program> findByStatus(ProgramStatus status, Pageable pageable);

    // This query is particular to PostgreSQL, might add room for upgrade to ElasticSearch
    @Query(
            value =
                    "SELECT * FROM programs " +
                            "WHERE status = 'PUBLISHED' " +
                            "AND to_tsvector('english', " +
                            "COALESCE(name, '') || ' ' || COALESCE(venue, '')) " +
                            "@@ plainto_tsquery('english', :searchTerm)",
            countQuery =
                    "SELECT count(*) FROM programs " +
                            "WHERE status = 'PUBLISHED' " +
                            "AND to_tsvector('english', " +
                            "COALESCE(name, '') || ' ' || COALESCE(venue, '')) " +
                            "@@ plainto_tsquery('english', :searchTerm)",
            nativeQuery = true
    )
    Page<Program> searchPrograms(@Param("searchTerm") String searchTerm, Pageable pageable);

    Optional<Program> findByIdAndStatus(UUID id, ProgramStatus status);
    
}
