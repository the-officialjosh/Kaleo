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
    String message() default  "start must be before end";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String startField() default "start";
    String endField() default "end";

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        StartBeforeEnd[] value();
    }
}
