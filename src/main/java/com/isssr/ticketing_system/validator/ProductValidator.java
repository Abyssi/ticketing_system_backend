package com.isssr.ticketing_system.validator;

import com.isssr.ticketing_system.model.Target;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Target.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Target team = (Target) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required", "Target name required");
        if (team.getName() != null && (team.getName().length() < 1))
            errors.rejectValue("name", "Insert a product name bigger than 1 char");
    }
}
