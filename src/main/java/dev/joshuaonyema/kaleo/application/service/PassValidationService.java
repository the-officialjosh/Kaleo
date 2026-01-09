package dev.joshuaonyema.kaleo.application.service;

import dev.joshuaonyema.kaleo.domain.entity.PassValidation;

import java.util.UUID;

public interface PassValidationService {
    PassValidation validatePassByQrCode(UUID programId, UUID qrCodeId);
    PassValidation validatePassByManualCode(UUID programId, String manualCode);
}
