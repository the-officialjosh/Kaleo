package dev.joshuaonyema.kaleo.api.controller;

import dev.joshuaonyema.kaleo.application.service.PassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/programs/{programId}/pass-types")
public class PassTypeController {

    private final PassService passService;

    @PostMapping("/{passTypeId}/passes")
    public ResponseEntity<Void> purchasePass(
            @PathVariable UUID programId,
            @PathVariable UUID passTypeId){
        passService.purchasePass(passTypeId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

}
