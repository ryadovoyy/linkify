package com.ryadovoy.linkify.advice;

public record ValidationError(String field, Object rejectedValue, String message) {
}
