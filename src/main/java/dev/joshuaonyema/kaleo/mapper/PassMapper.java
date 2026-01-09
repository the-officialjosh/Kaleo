package dev.joshuaonyema.kaleo.mapper;

import dev.joshuaonyema.kaleo.api.dto.response.GetPassResponseDto;
import dev.joshuaonyema.kaleo.api.dto.response.ListPassResponseDto;
import dev.joshuaonyema.kaleo.domain.entity.Pass;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PassMapper {

    @Mapping(source = "passType.name", target = "passTypeName")
    @Mapping(source = "passType.price", target = "passTypePrice")
    @Mapping(source = "passType.program.name", target = "programName")
    @Mapping(source = "passType.program.startTime", target = "programStartTime")
    @Mapping(source = "passType.program.endTime", target = "programEndTime")
    @Mapping(source = "passType.program.venue", target = "programVenue")
    ListPassResponseDto toListPassResponseDto(Pass pass);

    @Mapping(source = "passType.name", target = "passTypeName")
    @Mapping(source = "passType.price", target = "passTypePrice")
    @Mapping(source = "passType.description", target = "passTypeDescription")
    @Mapping(source = "passType.program.id", target = "programId")
    @Mapping(source = "passType.program.name", target = "programName")
    @Mapping(source = "passType.program.startTime", target = "programStartTime")
    @Mapping(source = "passType.program.endTime", target = "programEndTime")
    @Mapping(source = "passType.program.venue", target = "programVenue")
    GetPassResponseDto toGetPassResponseDto(Pass pass);
}
