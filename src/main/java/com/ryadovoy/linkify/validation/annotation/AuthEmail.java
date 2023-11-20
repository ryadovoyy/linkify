package com.ryadovoy.linkify.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotBlank;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@NotBlank(message = "Email is required")
public @interface AuthEmail {
    String message() default "{com.ryadovoy.linkify.validation.annotation.AuthEmail.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
