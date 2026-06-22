package com.chocksaway.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.ElementType.PARAMETER;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Documented
@Constraint(validatedBy = ValidLeavingReasonValidator.class)
@Target({FIELD, PARAMETER})
@Retention(RUNTIME)
public @interface ValidLeavingReason {
    String message() default "Invalid leaving reason";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

