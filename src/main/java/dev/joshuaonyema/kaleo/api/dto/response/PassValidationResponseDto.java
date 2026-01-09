package dev.joshuaonyema.kaleo.api.dto.response;

import dev.joshuaonyema.kaleo.domain.entity.PassValidationStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PassValidationResponseDto {
    private UUID passId;
    private PassValidationStatus status;
}
