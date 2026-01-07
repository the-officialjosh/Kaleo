package dev.joshuaonyema.kaleo.application.command;

import java.math.BigDecimal;

public interface PassTypeUpsertCommand {
     String getName();
     BigDecimal getPrice();
     String getDescription();
     Integer getTotalAvailable();
}
