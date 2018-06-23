package it.uniroma2.ticketingsystem.logger.aspect;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
//use as @LogClass(logAttrs={"attribute1", "attribute2"})
public @interface LogClass {
    String[] logAttrs() default "";
    String[] idAttrs() default "";
}
