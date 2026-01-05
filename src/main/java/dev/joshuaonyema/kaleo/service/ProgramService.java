package dev.joshuaonyema.kaleo.service;


import dev.joshuaonyema.kaleo.api.dto.CreateProgramRequest;
import dev.joshuaonyema.kaleo.domain.entity.Program;

public interface ProgramService {
    Program createProgram(CreateProgramRequest program);

}
