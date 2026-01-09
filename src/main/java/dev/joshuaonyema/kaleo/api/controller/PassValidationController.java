package dev.joshuaonyema.kaleo.api.controller;

import dev.joshuaonyema.kaleo.api.dto.request.PassValidationRequestDto;
import dev.joshuaonyema.kaleo.api.dto.response.PassValidationResponseDto;
import dev.joshuaonyema.kaleo.api.dto.response.StaffProgramResponseDto;
import dev.joshuaonyema.kaleo.application.service.PassValidationService;
import dev.joshuaonyema.kaleo.application.service.ProgramService;
import dev.joshuaonyema.kaleo.domain.entity.PassValidation;
import dev.joshuaonyema.kaleo.domain.entity.PassValidationMethod;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.mapper.PassValidationMapper;
import dev.joshuaonyema.kaleo.mapper.ProgramMapper;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/pass-validations")
@RequiredArgsConstructor
public class PassValidationController {
    private final PassValidationService passValidationService;
    private final PassValidationMapper passValidationMapper;
    private final ProgramService programService;
    private final ProgramMapper programMapper;

    @GetMapping
    public ResponseEntity<Page<StaffProgramResponseDto>> listPrograms(Pageable pageable) {
        Page<Program> programs = programService.listProgramsForStaff(pageable);
        return ResponseEntity.ok(programs.map(programMapper::toStaffProgramResponseDto));
    }

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
