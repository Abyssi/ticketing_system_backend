package com.isssr.ticketing_system.validator;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Collection;

public class ValidStringValidator implements ConstraintValidator<ValidString, String> {

    private Collection list;

    @Override
    public void initialize(ValidString constraintAnnotation) {
        this.list = Arrays.asList(constraintAnnotation.list());
    }

    @Override
    public boolean isValid(String s, ConstraintValidatorContext constraintValidatorContext) {
        return this.list.contains(s);
    }
}
