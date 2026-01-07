package dev.joshuaonyema.kaleo.application.command.common;

import java.math.BigDecimal;

public interface PassTypeUpsertCommand {
     String getName();
     BigDecimal getPrice();
     String getDescription();
     Integer getTotalAvailable();
}
