package dev.joshuaonyema.kaleo.api.controller;

import dev.joshuaonyema.kaleo.api.dto.response.ListPublishedProgramResponseDto;
import dev.joshuaonyema.kaleo.application.service.ProgramService;
import dev.joshuaonyema.kaleo.mapper.ProgramMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/published-programs")
@RequiredArgsConstructor
public class PublishedProgramController {

    private final ProgramMapper programMapper;
    private final ProgramService programService;

    @GetMapping
    public ResponseEntity<Page<ListPublishedProgramResponseDto>> listPublishedPrograms(Pageable pageable){
        return ResponseEntity.ok(programService.listPublishedPrograms(pageable)
                .map(programMapper::toListPublishedProgramResponseDto));
    }

}
