package dev.joshuaonyema.kaleo.api.controller;

import dev.joshuaonyema.kaleo.api.dto.response.ListPublishedProgramResponseDto;
import dev.joshuaonyema.kaleo.application.service.ProgramService;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.mapper.ProgramMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/v1/published-programs")
@RequiredArgsConstructor
public class PublishedProgramController {

    private final ProgramMapper programMapper;
    private final ProgramService programService;

    @GetMapping
    public ResponseEntity<Page<ListPublishedProgramResponseDto>> listPublishedPrograms(
            @RequestParam(required = false) String q,
            Pageable pageable){

        Page<Program> programs;

        if(null != q && !q.trim().isEmpty()){
            programs = programService.searchPublishedPrograms(q, pageable);
        }else {
            programs = programService.listPublishedPrograms(pageable);
        }

        return ResponseEntity.ok(programs
                .map(programMapper::toListPublishedProgramResponseDto));
    }

}
