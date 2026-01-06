package dev.joshuaonyema.kaleo.api.dto;

import dev.joshuaonyema.kaleo.api.validation.StartBeforeEnd;
import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@StartBeforeEnd(startField = "startTime", endField = "endTime")
@StartBeforeEnd(startField = "registrationStart", endField = "registrationEnd")
public class CreateProgramRequestDto {
    @NotBlank(message = "Program name is required")
    private String name;

    @NotNull(message = "Start Time is required")
    private LocalDateTime startTime;

    @NotNull(message = "End Time is required")
    private LocalDateTime endTime;

    @NotBlank(message = "Venue is required")
    private String venue;

    private LocalDateTime registrationStart;
    private LocalDateTime registrationEndTime;

    @NotNull(message = "Program Status is required")
    private ProgramStatus status;

    @NotEmpty(message = "At least one pass type required")
    @Valid
    private List<CreatePassTypeRequestDto> passTypes;

}
