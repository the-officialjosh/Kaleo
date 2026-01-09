package dev.joshuaonyema.kaleo.api.dto.response;

import dev.joshuaonyema.kaleo.domain.entity.PassStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPassResponseDto {
    private UUID id;
    private PassStatus status;
    private String manualCode;
    private LocalDateTime createdAt;

    // Pass Type Info
    private String passTypeName;
    private BigDecimal passTypePrice;
    private String passTypeDescription;

    // Program Info
    private UUID programId;
    private String programName;
    private LocalDateTime programStartTime;
    private LocalDateTime programEndTime;
    private String programVenue;
}
