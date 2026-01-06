package dev.joshuaonyema.kaleo.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

public class StartBeforeEndValidator implements ConstraintValidator<StartBeforeEnd, Object> {
    private  String startField;
    private String endField;

    @Override
    public void initialize(StartBeforeEnd constraintAnnotation) {
        this.startField = constraintAnnotation.startField();
        this.endField = constraintAnnotation.endField();
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if (o == null) return  true;

        try {
            Field sField = o.getClass().getDeclaredField(startField);
            Field eField = o.getClass().getDeclaredField(endField);

            sField.setAccessible(true);
            eField.setAccessible(true);

            LocalDateTime start = (LocalDateTime) sField.get(o);
            LocalDateTime end = (LocalDateTime) eField.get(o);

            if (start == null) return true;
            if (end == null) return  true;

            return start.isBefore(end);
        }catch (NoSuchFieldException | IllegalAccessException ex){
            return false;
        }
    }
}
