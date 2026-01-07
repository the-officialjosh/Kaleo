package dev.joshuaonyema.kaleo.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListPublishedProgramResponseDto {
    private UUID id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String venue;
}
