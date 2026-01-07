package dev.joshuaonyema.kaleo.mappers;

import dev.joshuaonyema.kaleo.api.dto.*;
import dev.joshuaonyema.kaleo.domain.entity.PassType;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import dev.joshuaonyema.kaleo.service.request.CreatePassTypeRequest;
import dev.joshuaonyema.kaleo.service.request.CreateProgramRequest;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProgramMapper {

    CreatePassTypeRequest fromDto(CreatePassTypeRequestDto dto);

    CreateProgramRequest fromDto(CreateProgramRequestDto dto);

    CreateProgramResponseDto toDto(Program program);

    ListPassTypeResponseDto toDto(PassType passType);

    ListProgramResponseDto toListProgramResponseDto(Program program);
}
