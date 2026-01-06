package dev.joshuaonyema.kaleo.api.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.lang.reflect.Field;

public class BothOrNoneValidator implements ConstraintValidator<BothOrNone, Object> {
    private  String first;
    private String second;

    @Override
    public void initialize(BothOrNone constraintAnnotation) {
        this.first = constraintAnnotation.first();
        this.second = constraintAnnotation.second();
    }

    @Override
    public boolean isValid(Object value, ConstraintValidatorContext constraintValidatorContext) {
       try{
           Field f1 = value.getClass().getDeclaredField(first);
           Field f2 = value.getClass().getDeclaredField(second);

           f1.setAccessible(true);
           f2.setAccessible(true);

           Object v1 = f1.get(value);
           Object v2 = f2.get(value);

           boolean bothNull = v1 == null && v2 == null;
           boolean bothPresent = v1 != null && v2 != null;

           return bothNull || bothPresent;
       }catch (Exception e){
           return false;
       }
    }
}
