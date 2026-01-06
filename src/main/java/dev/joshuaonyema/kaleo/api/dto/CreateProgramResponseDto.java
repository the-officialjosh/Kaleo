package dev.joshuaonyema.kaleo.api.dto;

import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import dev.joshuaonyema.kaleo.domain.entity.TimestampedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateProgramResponseDto extends TimestampedEntity {
    private UUID id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String venue;
    private LocalDateTime registrationStart;
    private LocalDateTime registrationEnd;
    private ProgramStatus status;
    private List<CreatePassTypeResponseDto> passTypes;
}
