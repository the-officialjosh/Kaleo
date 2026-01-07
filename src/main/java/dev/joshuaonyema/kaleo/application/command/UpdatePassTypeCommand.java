package dev.joshuaonyema.kaleo.application.command;

import dev.joshuaonyema.kaleo.application.command.common.PassTypeUpsertCommand;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdatePassTypeCommand implements PassTypeUpsertCommand {
    private UUID id;
    private String name;
    private BigDecimal price;
    private String description;
    private Integer totalAvailable;
}
