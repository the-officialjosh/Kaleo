package dev.joshuaonyema.kaleo.service;


import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.service.request.CreateProgramRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProgramService {
    Program createProgram(CreateProgramRequest program);
    Page<Program> listProgamsForOrganizer(Pageable pageable);
    Optional<Program> getProgramForOrganizer(UUID id);
}
