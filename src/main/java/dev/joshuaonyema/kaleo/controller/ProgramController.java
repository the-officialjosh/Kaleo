package dev.joshuaonyema.kaleo.controller;

import dev.joshuaonyema.kaleo.api.dto.CreateProgramRequestDto;
import dev.joshuaonyema.kaleo.api.dto.CreateProgramResponseDto;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.mappers.ProgramMapper;
import dev.joshuaonyema.kaleo.service.ProgramService;
import dev.joshuaonyema.kaleo.service.request.CreateProgramRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v{version}/programs")
@RequiredArgsConstructor
public class ProgramController {

    private final ProgramMapper programMapper;
    private final ProgramService programService;

    @PostMapping
    public ResponseEntity<CreateProgramResponseDto> createProgram(
            @PathVariable int version,
            @Valid @RequestBody CreateProgramRequestDto createProgramRequestDto
            )
    {
        CreateProgramRequest createProgramRequest = programMapper.fromDto(createProgramRequestDto);
        Program createdProgram = programService.createProgram(createProgramRequest);
        CreateProgramResponseDto createProgramResponseDto = programMapper.toDto(createdProgram);

        return new ResponseEntity<>(createProgramResponseDto, HttpStatus.CREATED);
    }
}
