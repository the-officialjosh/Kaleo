package dev.joshuaonyema.kaleo.api.controller;

import dev.joshuaonyema.kaleo.api.dto.request.PassValidationRequestDto;
import dev.joshuaonyema.kaleo.api.dto.response.PassValidationResponseDto;
import dev.joshuaonyema.kaleo.application.service.PassValidationService;
import dev.joshuaonyema.kaleo.domain.entity.PassValidation;
import dev.joshuaonyema.kaleo.domain.entity.PassValidationMethod;
import dev.joshuaonyema.kaleo.mapper.PassValidationMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pass-validations")
@RequiredArgsConstructor
public class PassValidationController {
    private final PassValidationService passValidationService;
    private final PassValidationMapper passValidationMapper;

    @PostMapping
    public ResponseEntity<PassValidationResponseDto> validatePass(
            @Valid @RequestBody PassValidationRequestDto request
    ) {
        PassValidation passValidation;

        if (PassValidationMethod.MANUAL.equals(request.getMethod())) {
            passValidation = passValidationService.validatePassByManualCode(
                    request.getProgramId(),
                    request.getManualCode()
            );
        } else {
            passValidation = passValidationService.validatePassByQrCode(
                    request.getProgramId(),
                    request.getQrCodeId()
            );
        }

        return ResponseEntity.ok(passValidationMapper.toDto(passValidation));
    }
}
