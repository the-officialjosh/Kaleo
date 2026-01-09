package dev.joshuaonyema.kaleo.mapper;

import dev.joshuaonyema.kaleo.api.dto.response.ListPassPassTypeInfoResponseDto;
import dev.joshuaonyema.kaleo.api.dto.response.ListPassProgramInfoResponseDto;
import dev.joshuaonyema.kaleo.api.dto.response.ListPassResponseDto;
import dev.joshuaonyema.kaleo.domain.entity.Pass;
import dev.joshuaonyema.kaleo.domain.entity.PassType;
import dev.joshuaonyema.kaleo.domain.entity.Program;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PassMapper {

    @Mapping(source = "passType", target = "passType")
    @Mapping(source = "passType.program", target = "program")
    ListPassResponseDto toListPassResponseDto(Pass pass);

    ListPassPassTypeInfoResponseDto toPassTypeInfo(PassType passType);

    ListPassProgramInfoResponseDto toProgramInfo(Program program);
}
