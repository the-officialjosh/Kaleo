package dev.joshuaonyema.kaleo.api.dto.request;

import dev.joshuaonyema.kaleo.api.validation.ValidPassValidationRequest;
import dev.joshuaonyema.kaleo.domain.entity.PassValidationMethod;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ValidPassValidationRequest
public class PassValidationRequestDto {
    @NotNull(message = "programId is required")
    private UUID programId;
    private UUID qrCodeId;
    private String manualCode;
    @NotNull(message = "method is required")
    private PassValidationMethod method;
}
