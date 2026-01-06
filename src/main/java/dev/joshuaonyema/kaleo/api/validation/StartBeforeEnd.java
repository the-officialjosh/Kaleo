package dev.joshuaonyema.kaleo.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StartBeforeEndValidator.class)
@Target(ElementType.TYPE)
@Repeatable(StartBeforeEnd.List.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface StartBeforeEnd {
    String message() default  "startTime must be before endTime";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String startField() default "startTime";
    String endField() default "endTime";

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        StartBeforeEnd[] value();
    }
}
