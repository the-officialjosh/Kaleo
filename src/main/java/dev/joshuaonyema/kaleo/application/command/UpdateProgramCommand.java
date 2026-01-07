package dev.joshuaonyema.kaleo.application.command;

import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateProgramCommand implements ProgramUpsertCommand{
    private UUID id;
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String venue;
    private LocalDateTime registrationStart;
    private LocalDateTime registrationEnd;
    private ProgramStatus status;
    private List<UpdatePassTypeCommand> passTypes;
}
