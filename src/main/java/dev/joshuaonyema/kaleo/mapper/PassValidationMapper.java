package dev.joshuaonyema.kaleo.mapper;

import dev.joshuaonyema.kaleo.api.dto.response.PassValidationResponseDto;
import dev.joshuaonyema.kaleo.domain.entity.PassValidation;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PassValidationMapper {

    @Mapping(source = "pass.id", target = "passId")
    @Mapping(source = "passStatus", target = "status")
    PassValidationResponseDto toDto(PassValidation passValidation);
}

