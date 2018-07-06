package com.isssr.ticketing_system.validator;

import com.isssr.ticketing_system.logger.entity.Record;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class RecordValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Record.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Record record = (Record) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required", "Team name required");
        if (record.getTag() != null && (record.getTag().length() < 1))
            errors.rejectValue("name", "Insert a team name bigger than 1 char");
    }
}
