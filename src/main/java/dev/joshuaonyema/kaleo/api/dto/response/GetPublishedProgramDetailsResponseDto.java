package dev.joshuaonyema.kaleo.api.dto.response;

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
public class GetPublishedProgramDetailsResponseDto {
    private UUID id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String venue;
    private List<GetPublishedPassTypesResponseDto> passTypes = new ArrayList<>();
    private LocalDateTime registrationStart;
    private LocalDateTime registrationEnd;
}
