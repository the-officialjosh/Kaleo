package dev.joshuaonyema.kaleo.api.controller;

import dev.joshuaonyema.kaleo.api.dto.request.CreateProgramRequestDto;
import dev.joshuaonyema.kaleo.api.dto.response.CreateProgramResponseDto;
import dev.joshuaonyema.kaleo.api.dto.response.GetProgramDetailsResponseDto;
import dev.joshuaonyema.kaleo.api.dto.response.ListProgramResponseDto;
import dev.joshuaonyema.kaleo.application.command.CreateProgramCommand;
import dev.joshuaonyema.kaleo.application.service.ProgramService;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.mapper.ProgramMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;


@RestController
@RequestMapping("/api/v1/programs")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramMapper programMapper;
    private final ProgramService programService;

    @PostMapping
    public ResponseEntity<CreateProgramResponseDto> createProgram(
            @Valid @RequestBody CreateProgramRequestDto createProgramRequestDto
            )
    {

        CreateProgramCommand createProgramRequest = programMapper.fromDto(createProgramRequestDto);
        Program createdProgram = programService.createProgram(createProgramRequest);
        CreateProgramResponseDto createProgramResponseDto = programMapper.toDto(createdProgram);

        return new ResponseEntity<>(createProgramResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ListProgramResponseDto>> listPrograms(Pageable pageable)
    {
        Page<Program> programs = programService.listProgramsForOrganizer(pageable);
        return ResponseEntity.ok(programs.map( programMapper::toListProgramResponseDto));
    }

    @GetMapping("/{programId}")
    public ResponseEntity<GetProgramDetailsResponseDto> getProgram(@PathVariable UUID programId)
    {
        return programService.getProgramForOrganizer(programId)
                .map(programMapper::toGetProgramDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
