package com.ryadovoy.linkify.validation.annotation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.FIELD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target(FIELD)
@Retention(RUNTIME)
@Constraint(validatedBy = {})
@NotNull(message = "Password is required")
@NoWhitespace(message = "Password must not contain whitespace characters")
@Size(min = 12, max = 64, message = "Password length must be between 12 and 64 characters")
@Pattern(
        regexp = "^(?=.*[A-Z])(?=.*[a-z])(?=.*\\d).*$",
        message = "Password must include at least one uppercase letter, one lowercase letter, and one digit")
public @interface RegistrationPassword {
    String message() default "{com.ryadovoy.linkify.validation.annotation.RegistrationPassword.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
