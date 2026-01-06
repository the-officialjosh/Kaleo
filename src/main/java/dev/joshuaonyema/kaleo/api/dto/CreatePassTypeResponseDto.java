package dev.joshuaonyema.kaleo.api.dto;

import dev.joshuaonyema.kaleo.domain.entity.TimestampedEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigInteger;
import java.util.UUID;

@Data
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
public class CreatePassTypeResponseDto extends TimestampedEntity {
    private UUID id;
    private String name;
    private BigInteger price;
    private String description;
    private Integer totalAvailable;
}
