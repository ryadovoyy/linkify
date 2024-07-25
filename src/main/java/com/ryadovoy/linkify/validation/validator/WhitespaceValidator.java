package com.ryadovoy.linkify.validation.validator;

import com.ryadovoy.linkify.validation.annotation.NoWhitespace;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

import java.util.regex.Pattern;

public class WhitespaceValidator implements ConstraintValidator<NoWhitespace, String> {
    private static final Pattern WHITESPACE_PATTERN = Pattern.compile("\\s");

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }
        return !WHITESPACE_PATTERN.matcher(value).find();
    }
}
