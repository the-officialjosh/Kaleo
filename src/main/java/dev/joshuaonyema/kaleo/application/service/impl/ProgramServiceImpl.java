package dev.joshuaonyema.kaleo.application.service.impl;

import dev.joshuaonyema.kaleo.application.command.CreateProgramCommand;
import dev.joshuaonyema.kaleo.application.command.UpdatePassTypeCommand;
import dev.joshuaonyema.kaleo.application.command.UpdateProgramCommand;
import dev.joshuaonyema.kaleo.application.command.common.PassTypeUpsertCommand;
import dev.joshuaonyema.kaleo.application.command.common.ProgramUpsertCommand;
import dev.joshuaonyema.kaleo.application.security.CurrentUserService;
import dev.joshuaonyema.kaleo.application.service.ProgramService;
import dev.joshuaonyema.kaleo.domain.entity.PassType;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.domain.entity.ProgramStatus;
import dev.joshuaonyema.kaleo.domain.entity.User;
import dev.joshuaonyema.kaleo.exception.PassTypeNotFoundException;
import dev.joshuaonyema.kaleo.exception.ProgramNotFoundException;
import dev.joshuaonyema.kaleo.repository.ProgramRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {

    private final CurrentUserService currentUserService;
    private final ProgramRepository programRepository;

    @Override
    @PreAuthorize("hasRole('ORGANIZER')")
    @Transactional
    public Program createProgram(CreateProgramCommand command) {
        User organizer = currentUserService.getCurrentUser();
        Program program = new Program();
        program.setOrganizer(organizer);
        applyProgramFields(program, command);
        if (command.getPassTypes() != null) {
            List<PassType> passTypes = command.getPassTypes().stream()
                    .map(passType -> newPassType(program, passType))
                    .toList();
            program.setPassTypes(passTypes);
        }
        return programRepository.save(program);
    }

    @Override
    @PreAuthorize("hasRole('ORGANIZER')")
    public Page<Program> listProgramsForOrganizer(Pageable pageable) {
        User organizer = currentUserService.getCurrentUser();
        return programRepository.findByOrganizer(organizer, pageable);
    }

    @Override
    @PreAuthorize("hasRole('ORGANIZER')")
    public Optional<Program> getProgramForOrganizer(UUID id) {
        User organizer = currentUserService.getCurrentUser();
        return programRepository.findByIdAndOrganizer(id, organizer);
    }

    @Override
    @PreAuthorize("hasRole('ORGANIZER')")
    @Transactional
    public Program updateProgramForOrganizer(UUID id, UpdateProgramCommand command) {
        validateUpdateCommand(id, command);
        Program program = getOwnedProgram(id);
        applyProgramFields(program, command);
        if (command.getPassTypes() != null) {
            syncPassTypes(program, command.getPassTypes());
        }
        return programRepository.save(program);
    }

    @Override
    @Transactional
    @PreAuthorize("hasRole('ORGANIZER')")
    public void deleteProgramForOrganizer(UUID id) {
        Program program = getOwnedProgram(id);
        programRepository.delete(program);
    }

    @Override
    @Transactional
    public Page<Program> listPublishedPrograms(Pageable pageable) {
        return programRepository.findByStatus(ProgramStatus.PUBLISHED, pageable);
    }

    @Override
    public Page<Program> searchPublishedPrograms(String query, Pageable pageable) {
        return programRepository.searchPrograms(query, pageable);
    }

    @Override
    public Optional<Program> getPublishedProgram(UUID id) {
        return programRepository.findByIdAndStatus(id, ProgramStatus.PUBLISHED);
    }

    private Program getOwnedProgram(UUID id) {
        User organizer = currentUserService.getCurrentUser();
        return programRepository.findByIdAndOrganizer(id, organizer)
                .orElseThrow(() -> new ProgramNotFoundException(
                        String.format("Program with ID '%s' not found or you don't have access", id)
                ));
    }

    private void validateUpdateCommand(UUID pathId, UpdateProgramCommand command) {
        if (command.getId() == null) {
            throw new dev.joshuaonyema.kaleo.exception.ProgramUpdateException("Program ID is required in the request body");
        }
        if (!pathId.equals(command.getId())) {
            throw new dev.joshuaonyema.kaleo.exception.ProgramUpdateException(
                    String.format("Path ID '%s' does not match request body ID '%s'",
                            pathId, command.getId())
            );
        }
    }

    private void applyProgramFields(Program program, ProgramUpsertCommand c) {
        program.setName(c.getName());
        program.setStartTime(c.getStartTime());
        program.setEndTime(c.getEndTime());
        program.setVenue(c.getVenue());
        program.setRegistrationStart(c.getRegistrationStart());
        program.setRegistrationEnd(c.getRegistrationEnd());
        program.setStatus(c.getStatus());
    }

    private PassType newPassType(Program program, PassTypeUpsertCommand c) {
        PassType passType = new PassType();
        passType.setProgram(program);
        applyPassTypeFields(passType, c);
        return passType;
    }

    private void applyPassTypeFields(PassType passType, PassTypeUpsertCommand c) {
        passType.setName(c.getName());
        passType.setPrice(c.getPrice());
        passType.setDescription(c.getDescription());
        passType.setTotalAvailable(c.getTotalAvailable());
    }

    private void syncPassTypes(Program program, List<UpdatePassTypeCommand> commands) {
        Map<UUID, PassType> existingById = program.getPassTypes().stream()
                .filter(passType -> passType.getId() != null)
                .collect(Collectors.toMap(PassType::getId, Function.identity()));

        Set<UUID> requestedIds = commands.stream()
                .map(UpdatePassTypeCommand::getId)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());

        program.getPassTypes().removeIf(passType ->
                passType.getId() != null && !requestedIds.contains(passType.getId()));

        for (UpdatePassTypeCommand c : commands) {
            if (c.getId() == null) {
                program.getPassTypes().add(newPassType(program, c));
                continue;
            }

            PassType existing = existingById.get(c.getId());
            if (existing == null) {
                throw new PassTypeNotFoundException(
                        String.format("PassType with ID '%s' not found or you don't have access", c.getId())
                );
            }

            applyPassTypeFields(existing, c);
        }
    }
}