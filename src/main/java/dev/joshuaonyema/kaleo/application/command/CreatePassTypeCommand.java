package dev.joshuaonyema.kaleo.application.command;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CreatePassTypeCommand {
    private String name;
    private BigDecimal price;
    private String description;
    private Integer totalAvailable;
}
