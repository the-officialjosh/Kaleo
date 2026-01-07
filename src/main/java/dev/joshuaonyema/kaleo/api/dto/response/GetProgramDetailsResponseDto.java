package dev.joshuaonyema.kaleo.api.dto.response;

import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetProgramDetailsResponseDto {
    private UUID id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String venue;
    private LocalDateTime registrationStart;
    private LocalDateTime registrationEnd;
    private ProgramStatus status;
    private List<GetPassTypesResponseDto> passTypes = new ArrayList<>();
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
