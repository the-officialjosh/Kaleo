package dev.joshuaonyema.kaleo.application.service;


import dev.joshuaonyema.kaleo.application.command.CreateProgramCommand;
import dev.joshuaonyema.kaleo.application.command.UpdateProgramCommand;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface ProgramService {
    Program createProgram(CreateProgramCommand program);
    Page<Program> listProgramsForOrganizer(Pageable pageable);
    Optional<Program> getProgramForOrganizer(UUID id);
    Program updateProgramForOrganizer(UUID id, UpdateProgramCommand program);
    void deleteProgramForOrganizer(UUID id);
    Page<Program> listPublishedEvent(Pageable pageable);
}
