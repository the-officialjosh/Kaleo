package dev.joshuaonyema.kaleo.mapper;

import dev.joshuaonyema.kaleo.api.dto.request.CreatePassTypeRequestDto;
import dev.joshuaonyema.kaleo.api.dto.request.CreateProgramRequestDto;
import dev.joshuaonyema.kaleo.api.dto.response.*;
import dev.joshuaonyema.kaleo.application.command.CreatePassTypeCommand;
import dev.joshuaonyema.kaleo.application.command.CreateProgramCommand;
import dev.joshuaonyema.kaleo.domain.entity.PassType;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import org.mapstruct.Mapper;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface ProgramMapper {

    CreatePassTypeCommand fromDto(CreatePassTypeRequestDto dto);

    CreateProgramCommand fromDto(CreateProgramRequestDto dto);

    CreateProgramResponseDto toDto(Program program);

    ListPassTypeResponseDto toDto(PassType passType);

    ListProgramResponseDto toListProgramResponseDto(Program program);

    GetPassTypesResponseDto toGetPassTypesResponseDto(PassType passType);

    GetProgramDetailsResponseDto toGetProgramDetailsResponseDto(Program program);
}
