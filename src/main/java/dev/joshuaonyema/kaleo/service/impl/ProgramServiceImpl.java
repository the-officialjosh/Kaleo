package dev.joshuaonyema.kaleo.service.impl;

import dev.joshuaonyema.kaleo.domain.entity.PassType;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.domain.entity.User;
import dev.joshuaonyema.kaleo.exception.UserNotFoundException;
import dev.joshuaonyema.kaleo.repository.ProgramRepository;
import dev.joshuaonyema.kaleo.repository.UserRepository;
import dev.joshuaonyema.kaleo.service.ProgramService;
import dev.joshuaonyema.kaleo.service.request.CreateProgramRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProgramServiceImpl implements ProgramService {

    private final UserRepository userRepository;
    private final ProgramRepository programRepository;

    @Override
    @PreAuthorize("hasRole('ORGANIZER')")
    public Program createProgram(CreateProgramRequest programRequest) {
        Program programToCreate = new Program();

        programToCreate.setOrganizer(currentUser());
        programToCreate.setName(programRequest.getName());
        programToCreate.setStartTime(programRequest.getStartTime());
        programToCreate.setEndTime(programRequest.getEndTime());
        programToCreate.setVenue(programRequest.getVenue());
        programToCreate.setRegistrationStart(programRequest.getRegistrationStart());
        programToCreate.setRegistrationEnd(programRequest.getRegistrationEnd());
        programToCreate.setStatus(programRequest.getStatus());
        programToCreate.setPassTypes(createPassTypes(programRequest, programToCreate));

        return programRepository.save(programToCreate);
    }

    @Override
    @PreAuthorize("hasRole('ORGANIZER')")
    public Page<Program> listProgamsForOrganizer(Pageable pageable) {
        return programRepository.findByOrganizer(currentUser(), pageable);
    }

    @Override
    @PreAuthorize("hasRole('ORGANIZER')")
    public Optional<Program> getProgramForOrganizer(UUID id) {
        return programRepository.findByIdAndOrganizer(id, currentUser());
    }

    private static List<PassType> createPassTypes(CreateProgramRequest programRequest, Program program) {
        return programRequest.getPassTypes().stream().map(passType ->
        {
            PassType passTypeToCreate = new PassType();
            passTypeToCreate.setName(passType.getName());
            passTypeToCreate.setPrice(passType.getPrice());
            passTypeToCreate.setDescription(passType.getDescription());
            passTypeToCreate.setTotalAvailable(passType.getTotalAvailable());
            passTypeToCreate.setProgram(program);
            return passTypeToCreate;
        }).toList();
    }

    private User currentUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !(auth.getPrincipal() instanceof Jwt jwt)){
            throw  new AuthenticationCredentialsNotFoundException("Not authenticated");
        }


        UUID organizerId = UUID.fromString(jwt.getSubject());
        return userRepository.findById(organizerId)
                .orElseThrow(()-> new UserNotFoundException(String.format("User with ID '%s' not found", organizerId)));
    }
}

