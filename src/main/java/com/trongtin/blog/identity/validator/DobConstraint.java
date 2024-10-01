package com.trongtin.blog.identity.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = {DobValidator.class})
public @interface DobConstraint {

    String message() default "INVALID_DOB";
    int min(); // gia tri toi thieu cua age
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
