package dev.joshuaonyema.kaleo.api.controller;

import dev.joshuaonyema.kaleo.api.dto.response.GetPassResponseDto;
import dev.joshuaonyema.kaleo.api.dto.response.ListPassResponseDto;
import dev.joshuaonyema.kaleo.application.service.PassService;
import dev.joshuaonyema.kaleo.mapper.PassMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/passes")
@RequiredArgsConstructor
public class PassController {

    private final PassService passService;
    private final PassMapper passMapper;

    @GetMapping
    public Page<ListPassResponseDto> listPasses(Pageable pageable){
        return passService.listPassesForUser(pageable).map(passMapper::toListPassResponseDto);
    }

    @GetMapping("/{passId}")
    public ResponseEntity<GetPassResponseDto> getPass(
            @PathVariable UUID passId){
        return passService.getPassForUser(passId)
                .map(passMapper::toGetPassResponseDto)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
 }
