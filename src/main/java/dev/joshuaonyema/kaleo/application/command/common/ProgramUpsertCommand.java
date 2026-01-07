package dev.joshuaonyema.kaleo.application.command.common;

import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;

import java.time.LocalDateTime;

public interface ProgramUpsertCommand {
    String getName();
    LocalDateTime getStartTime();
    LocalDateTime getEndTime();
    String getVenue();
    LocalDateTime getRegistrationStart();
    LocalDateTime getRegistrationEnd();
    ProgramStatus getStatus();
}