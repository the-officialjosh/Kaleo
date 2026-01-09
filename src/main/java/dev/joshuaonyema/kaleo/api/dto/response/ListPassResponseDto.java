package dev.joshuaonyema.kaleo.api.dto.response;

import dev.joshuaonyema.kaleo.domain.entity.PassStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListPassResponseDto {
    private UUID id;
    private PassStatus status;
    private LocalDateTime createdAt;
    private ListPassPassTypeInfoResponseDto passType;
    private ListPassProgramInfoResponseDto program;
}
