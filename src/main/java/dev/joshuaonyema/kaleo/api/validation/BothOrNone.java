package dev.joshuaonyema.kaleo.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = StartBeforeEndValidator.class)
@Target(ElementType.TYPE)
@Repeatable(BothOrNone.List.class)
@Retention(RetentionPolicy.RUNTIME)
public @interface BothOrNone {
    String message() default  "Both fields must be set or both must be null";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String first() default "first";
    String second() default "second";

    @Target(ElementType.TYPE)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    @interface List {
        BothOrNone[] value();
    }
}
