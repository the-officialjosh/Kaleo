package dev.joshuaonyema.kaleo.controller;

import dev.joshuaonyema.kaleo.api.dto.CreateProgramRequestDto;
import dev.joshuaonyema.kaleo.api.dto.CreateProgramResponseDto;
import dev.joshuaonyema.kaleo.api.dto.GetProgramDetailsResponseDto;
import dev.joshuaonyema.kaleo.api.dto.ListProgramResponseDto;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.mappers.ProgramMapper;
import dev.joshuaonyema.kaleo.service.ProgramService;
import dev.joshuaonyema.kaleo.service.request.CreateProgramRequest;
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

        CreateProgramRequest createProgramRequest = programMapper.fromDto(createProgramRequestDto);
        Program createdProgram = programService.createProgram(createProgramRequest);
        CreateProgramResponseDto createProgramResponseDto = programMapper.toDto(createdProgram);

        return new ResponseEntity<>(createProgramResponseDto, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<Page<ListProgramResponseDto>> listPrograms(Pageable pageable)
    {
        Page<Program> programs = programService.listProgamsForOrganizer(pageable);
        return ResponseEntity.ok(programs.map( programMapper::toListProgramResponseDto));
    }

    @GetMapping("/{programId}")
    public ResponseEntity<GetProgramDetailsResponseDto> getEvent(@PathVariable UUID programId)
    {
        return programService.getProgramForOrganizer(programId)
                .map(programMapper::toGetProgramDetailsResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
