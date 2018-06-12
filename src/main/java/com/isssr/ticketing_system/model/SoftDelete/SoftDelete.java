package com.isssr.ticketing_system.model.SoftDelete;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface SoftDelete {
    SoftDeleteKind value() default SoftDeleteKind.NOT_DELETED;
}
