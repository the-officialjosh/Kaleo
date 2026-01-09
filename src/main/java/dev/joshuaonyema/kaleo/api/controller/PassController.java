package dev.joshuaonyema.kaleo.api.controller;

import dev.joshuaonyema.kaleo.api.dto.response.ListPassResponseDto;
import dev.joshuaonyema.kaleo.application.service.PassService;
import dev.joshuaonyema.kaleo.mapper.PassMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/passes")
@RequiredArgsConstructor
public class PassController {

    private final PassService passService;
    private final PassMapper passMapper;

    @GetMapping
    public Page<ListPassResponseDto> listPasses(
            Pageable pageable
    ){
        return passService.listPassesForUser(pageable).map(passMapper::toListPassResponseDto);
    }
 }
