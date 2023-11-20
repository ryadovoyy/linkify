package com.ryadovoy.linkify.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryadovoy.linkify.exception.AuthenticationException;
import com.ryadovoy.linkify.exception.UserAlreadyExistsException;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse<Violation>> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        List<Violation> violations = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            Violation violation = new Violation(error.getField(), error.getRejectedValue(), error.getDefaultMessage());
            violations.add(violation);
        });

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        String message = "Validation errors occurred";
        ErrorResponse<Violation> response = new ErrorResponse<>(httpStatus, message, request, violations);

        return ResponseEntity.status(httpStatus).body(response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse<?>> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex, WebRequest request) {
        return createSimpleResponse(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse<?>> handleAuthenticationException(
            AuthenticationException ex, WebRequest request) {
        return createSimpleResponse(HttpStatus.UNAUTHORIZED, ex, request);
    }

    private static ResponseEntity<ErrorResponse<?>> createSimpleResponse(
            HttpStatus httpStatus, Exception ex, WebRequest request) {
        ErrorResponse<?> response = new ErrorResponse<>(httpStatus, ex.getMessage(), request);
        return ResponseEntity.status(httpStatus).body(response);
    }

    @Getter
    public static class ErrorResponse<T> {
        private final Date timestamp = new Date();
        private final int status;
        private final String error;
        private final String message;
        private final String path;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<T> subErrors;

        public ErrorResponse(HttpStatus httpStatus, String message, WebRequest request) {
            this.status = httpStatus.value();
            this.error = httpStatus.getReasonPhrase();
            this.message = message;
            this.path = request.getDescription(false).split("=")[1];
        }

        public ErrorResponse(HttpStatus httpStatus, String message, WebRequest request, List<T> subErrors) {
            this(httpStatus, message, request);
            this.subErrors = subErrors;
        }
    }

    public record Violation(String field, Object rejectedValue, String message) {
    }
}
