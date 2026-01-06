package dev.joshuaonyema.kaleo.service;


import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.service.request.CreateProgramRequest;

public interface ProgramService {
    Program createProgram(CreateProgramRequest program);

}
