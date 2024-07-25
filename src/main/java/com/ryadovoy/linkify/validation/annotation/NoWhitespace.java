package com.ryadovoy.linkify.validation.annotation;

import com.ryadovoy.linkify.validation.validator.WhitespaceValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ METHOD, FIELD, ANNOTATION_TYPE, CONSTRUCTOR, PARAMETER, TYPE_USE })
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = WhitespaceValidator.class)
public @interface NoWhitespace {
    String message() default "{com.ryadovoy.linkify.validation.annotation.NoWhitespace.message}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
