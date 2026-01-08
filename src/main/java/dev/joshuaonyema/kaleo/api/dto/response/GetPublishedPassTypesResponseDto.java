package dev.joshuaonyema.kaleo.api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class GetPublishedPassTypesResponseDto {
    private UUID id;
    private String name;
    private BigDecimal price;
    private String description;
}
