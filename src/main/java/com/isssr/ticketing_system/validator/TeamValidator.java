package com.isssr.ticketing_system.validator;

import com.isssr.ticketing_system.model.Team;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

@Component
public class TeamValidator implements Validator {
    @Override
    public boolean supports(Class<?> aClass) {
        return Team.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        Team team = (Team) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "name", "name.required", "Team name required");
        if (team.getName() != null && (team.getName().length() < 1))
            errors.rejectValue("name", "Insert a team name bigger than 1 char");
    }
}
