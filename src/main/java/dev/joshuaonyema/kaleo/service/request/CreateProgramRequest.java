package dev.joshuaonyema.kaleo.service.request;

import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreateProgramRequest {
    private String name;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String venue;
    private LocalDateTime registrationStart;
    private LocalDateTime registrationEndTime;
    private ProgramStatus status;
    private List<CreatePassTypeRequest> passTypes = new ArrayList<>();

    // TODO: When implementing ProgramService#create,
    //  set Program.organizer from SecurityContext.
    //  Organizer is derived from the authenticated principal (JWT)
    //  in the service layer.

}
