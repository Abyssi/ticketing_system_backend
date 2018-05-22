package com.isssr.ticketing_system.validator;

import com.isssr.ticketing_system.model.Product;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class ProductValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Product.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Product team = (Product) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required", "Product name required");
        if (team.getName() != null && (team.getName().length() < 1))
            errors.rejectValue("name", "Insert a product name bigger than 1 char");
    }
}
