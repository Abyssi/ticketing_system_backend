package com.isssr.ticketing_system.validator;

import com.isssr.ticketing_system.model.User;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import java.util.regex.Pattern;

@Component
public class UserValidator implements Validator {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    @Override
    public boolean supports(Class<?> aClass) {
        return User.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        User user = (User) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "email.required", "Email required");
        if (user.getEmail() != null && (!validateEmail(user.getEmail())))
            errors.rejectValue("email", "Invalid email");

        /*ValidationUtils.rejectIfEmptyOrWhitespace(errors, "password", "password.required", "Password required");
        if (user.getPassword() != null && (user.getPassword().length() < 8 || user.getPassword().length() > 32))
            errors.rejectValue("password", "Use a password between 8 and 32 chars");*/

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "firstName", "firstName.required", "First name required");
        if (user.getFirstName() != null && (user.getFirstName().length() < 1))
            errors.rejectValue("firstName", "Insert a first name bigger than 1 char");

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "lastName", "lastName.required", "Last name required");
        if (user.getLastName() != null && (user.getLastName().length() < 1))
            errors.rejectValue("lastName", "Insert a last name bigger than 1 char");
    }

    private boolean validateEmail(final String email) {
        return Pattern.compile(EMAIL_PATTERN).matcher(email).matches();
    }
}
