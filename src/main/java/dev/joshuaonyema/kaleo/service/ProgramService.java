package dev.joshuaonyema.kaleo.service;


import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.service.request.CreateProgramRequest;
import org.springframework.data.domain.Page;

public interface ProgramService {
    Program createProgram(CreateProgramRequest program);
    Page<Program> listEventsForOrganizer();
}
