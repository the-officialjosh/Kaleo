package dev.joshuaonyema.kaleo.application.service.impl;

import dev.joshuaonyema.kaleo.application.service.PassValidationService;
import dev.joshuaonyema.kaleo.domain.entity.*;
import dev.joshuaonyema.kaleo.exception.CodeNotFoundException;
import dev.joshuaonyema.kaleo.exception.ProgramPassException;
import dev.joshuaonyema.kaleo.exception.QrCodeNotFoundException;
import dev.joshuaonyema.kaleo.repository.PassRepository;
import dev.joshuaonyema.kaleo.repository.PassValidationRepository;
import dev.joshuaonyema.kaleo.repository.QrCodeRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PassValidationServiceImpl implements PassValidationService {

    private final QrCodeRepository qrCodeRepository;
    private final PassValidationRepository passValidationRepository;
    private final PassRepository passRepository;


    @Override
    public PassValidation validatePassByQrCode(UUID programId, UUID qrCodeId) {
        QrCode qrCode = qrCodeRepository.findByIdAndStatus(qrCodeId, QrCodeStatus.ACTIVE)
                .orElseThrow(() -> new QrCodeNotFoundException(
                        String.format("QR Code with ID %s was not found", qrCodeId)
                ));
        Pass pass = qrCode.getPass();
        validatePassBelongsToProgram(pass, programId);
        return getPassValidation(pass, PassValidationMethod.QR_SCAN);
    }

    @Override
    public PassValidation validatePassByManualCode(UUID programId, String manualCode) {
        Pass pass = passRepository.findByManualCode(manualCode)
                .orElseThrow(CodeNotFoundException::new);
        validatePassBelongsToProgram(pass, programId);
        return getPassValidation(pass, PassValidationMethod.MANUAL);
    }

    private void validatePassBelongsToProgram(Pass pass, UUID programId) {
        UUID passProgramId = pass.getPassType().getProgram().getId();
        if (!passProgramId.equals(programId)) {
            throw new ProgramPassException(
                    String.format("Pass does not belong to program with ID %s", programId)
            );
        }
    }

    private @NonNull PassValidation getPassValidation(Pass pass, PassValidationMethod passValidationMethod) {
        PassValidation passValidation = new PassValidation();
        passValidation.setPass(pass);
        passValidation.setValidationMethod(passValidationMethod);

        PassValidationStatus passValidationStatus = pass.getPassValidations().stream()
                .filter(v -> PassValidationStatus.VALID.equals(v.getPassStatus()))
                .findFirst().map(v -> PassValidationStatus.INVALID)
                .orElse(PassValidationStatus.VALID);

        passValidation.setPassStatus(passValidationStatus);

        return passValidationRepository.save(passValidation);
    }
}
