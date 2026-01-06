package dev.joshuaonyema.kaleo.service.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePassTypeRequest {
    private String name;
    private BigDecimal price;
    private String description;
    private Integer totalAvailable;
}
