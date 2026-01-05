package dev.joshuaonyema.kaleo.api.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigInteger;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePassTypeRequest {
    private String name;
    private BigInteger price;
    private String description;
    private Integer totalAvailable;
}
