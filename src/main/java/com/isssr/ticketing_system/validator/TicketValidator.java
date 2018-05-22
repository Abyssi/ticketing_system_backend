package com.isssr.ticketing_system.validator;

import com.isssr.ticketing_system.model.Ticket;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TicketValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Ticket.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Ticket team = (Ticket) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required", "Ticket name required");
        if (team.getTitle() != null && (team.getTitle().length() < 1))
            errors.rejectValue("name", "Insert a ticket name bigger than 1 char");
    }
}
