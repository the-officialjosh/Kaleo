package dev.joshuaonyema.kaleo.api.validation;

import dev.joshuaonyema.kaleo.api.dto.request.PassValidationRequestDto;
import dev.joshuaonyema.kaleo.domain.entity.PassValidationMethod;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PassValidationRequestValidator
        implements ConstraintValidator<ValidPassValidationRequest, PassValidationRequestDto> {

    @Override
    public boolean isValid(PassValidationRequestDto value, ConstraintValidatorContext context) {
        if (value == null) return true;

        PassValidationMethod method = value.getMethod();
        boolean hasQrId = value.getQrCodeId() != null;

        String manual = value.getManualCode();
        boolean hasManual = manual != null && !manual.trim().isEmpty();

        if (method == null) return true; // let @NotNull on method handle it

        context.disableDefaultConstraintViolation();

        if (method == PassValidationMethod.QR_SCAN) {
            boolean ok = hasQrId && !hasManual;

            if (!hasQrId) {
                context.buildConstraintViolationWithTemplate("qrCodeId is required when method is QR")
                        .addPropertyNode("qrCodeId")
                        .addConstraintViolation();
            }
            if (hasManual) {
                context.buildConstraintViolationWithTemplate("manualCode must be omitted when method is QR")
                        .addPropertyNode("manualCode")
                        .addConstraintViolation();
            }
            return ok;
        }

        if (method == PassValidationMethod.MANUAL) {
            boolean ok = hasManual && !hasQrId;

            if (!hasManual) {
                context.buildConstraintViolationWithTemplate("manualCode is required when method is MANUAL")
                        .addPropertyNode("manualCode")
                        .addConstraintViolation();
            }
            if (hasQrId) {
                context.buildConstraintViolationWithTemplate("qrCodeId must be omitted when method is MANUAL")
                        .addPropertyNode("qrCodeId")
                        .addConstraintViolation();
            }
            return ok;
        }

        context.buildConstraintViolationWithTemplate("Unsupported method")
                .addPropertyNode("method")
                .addConstraintViolation();
        return false;
    }
}