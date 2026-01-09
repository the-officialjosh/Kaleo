package dev.joshuaonyema.kaleo.api.dto.response;

import dev.joshuaonyema.kaleo.domain.entity.PassStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListPassResponseDto {
    private PassStatus status;
    private LocalDateTime createdAt;

    // Pass Type Info
    private String passTypeName;
    private BigDecimal passTypePrice;
    private String passTypeDescription;

    // Program Info
    private String programName;
    private LocalDateTime programStartTime;
    private LocalDateTime programEndTime;
    private String programVenue;
}
