package dev.joshuaonyema.kaleo.api.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PassValidationRequestValidator.class)
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassValidationRequest {
    String message() default "Invalid pass validation request";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}