package com.ryadovoy.linkify.advice;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ryadovoy.linkify.exception.PageArgumentNotValidException;
import com.ryadovoy.linkify.exception.RoleNotFoundException;
import com.ryadovoy.linkify.exception.UserAlreadyExistsException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler {
    private static final String VALIDATION_ERROR_MESSAGE = "Validation errors occurred";
    private static final String REGISTRATION_ERROR_MESSAGE = "Registration failed";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "There is a server problem, try again later";

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationException(
            MethodArgumentNotValidException ex, WebRequest request) {
        log.info(ex.getMessage());
        List<Violation> violations = new ArrayList<>();

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            Violation violation = new Violation(error.getField(), error.getRejectedValue(), error.getDefaultMessage());
            violations.add(violation);
        });

        HttpStatus httpStatus = HttpStatus.BAD_REQUEST;
        ErrorResponse response = new ErrorResponse(httpStatus, VALIDATION_ERROR_MESSAGE, request, violations);

        return createResponseEntity(httpStatus, response);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<ErrorResponse> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex, WebRequest request) {
        log.info(ex.getMessage());
        return createSimpleResponse(HttpStatus.BAD_REQUEST, REGISTRATION_ERROR_MESSAGE, request);
    }

    @ExceptionHandler(PageArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handlePageArgumentNotValidException(
            PageArgumentNotValidException ex, WebRequest request) {
        log.info(ex.getMessage());
        return createSimpleResponse(HttpStatus.BAD_REQUEST, ex, request);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return createSimpleResponse(HttpStatus.UNAUTHORIZED, ex, request);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleRoleNotFoundException(RoleNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return createSimpleResponse(HttpStatus.INTERNAL_SERVER_ERROR, INTERNAL_SERVER_ERROR_MESSAGE, request);
    }

    private static ResponseEntity<ErrorResponse> createSimpleResponse(
            HttpStatus httpStatus, Exception ex, WebRequest request) {
        ErrorResponse response = new ErrorResponse(httpStatus, ex.getMessage(), request);
        return createResponseEntity(httpStatus, response);
    }

    private static ResponseEntity<ErrorResponse> createSimpleResponse(
            HttpStatus httpStatus, String message, WebRequest request) {
        ErrorResponse response = new ErrorResponse(httpStatus, message, request);
        return createResponseEntity(httpStatus, response);
    }

    private static ResponseEntity<ErrorResponse> createResponseEntity(HttpStatus httpStatus, ErrorResponse response) {
        return ResponseEntity.status(httpStatus).body(response);
    }

    @Getter
    public static class ErrorResponse {
        private final Date timestamp = new Date();
        private final int status;
        private final String error;
        private final String message;
        private final String path;

        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<Violation> subErrors;

        public ErrorResponse(HttpStatus httpStatus, String message, WebRequest request) {
            this.status = httpStatus.value();
            this.error = httpStatus.getReasonPhrase();
            this.message = message;
            this.path = request.getDescription(false).split("=")[1];
        }

        public ErrorResponse(HttpStatus httpStatus, String message, WebRequest request, List<Violation> subErrors) {
            this(httpStatus, message, request);
            this.subErrors = subErrors;
        }
    }

    public record Violation(String field, Object rejectedValue, String message) {
    }
}
