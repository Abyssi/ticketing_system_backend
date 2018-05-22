package com.isssr.ticketing_system.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Target({ElementType.METHOD, ElementType.FIELD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.TYPE_USE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Constraint(validatedBy = ValidStringValidator.class)
public @interface ValidString {

    String message() default "Value not accepted";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    String[] list() default {};

}
