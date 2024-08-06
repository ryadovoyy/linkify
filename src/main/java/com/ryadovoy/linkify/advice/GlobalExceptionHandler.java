package com.ryadovoy.linkify.advice;

import com.ryadovoy.linkify.exception.PageArgumentNotValidException;
import com.ryadovoy.linkify.exception.RoleNotFoundException;
import com.ryadovoy.linkify.exception.UserAlreadyExistsException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {
    private static final String VALIDATION_ERROR_MESSAGE = "Validation errors occurred";
    private static final String REGISTRATION_ERROR_MESSAGE = "Registration failed";
    private static final String INTERNAL_SERVER_ERROR_MESSAGE = "There is a server problem, try again later";

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        log.info(ex.getMessage());
        ErrorResponse errorResponse = new ErrorResponse(VALIDATION_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);

        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errorResponse.addValidationError(
                    new ValidationError(error.getField(), error.getRejectedValue(), error.getDefaultMessage()));
        });

        return createResponseEntity(errorResponse, headers, status, request);
    }

    @ExceptionHandler(UserAlreadyExistsException.class)
    public ResponseEntity<Object> handleUserAlreadyExistsException(
            UserAlreadyExistsException ex, WebRequest request) {
        log.info(ex.getMessage());
        return handleCustomException(ex, request, REGISTRATION_ERROR_MESSAGE, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(PageArgumentNotValidException.class)
    public ResponseEntity<Object> handlePageArgumentNotValidException(
            PageArgumentNotValidException ex, WebRequest request) {
        log.info(ex.getMessage());
        return handleCustomException(ex, request, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<Object> handleAuthenticationException(AuthenticationException ex, WebRequest request) {
        return handleCustomException(ex, request, HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler(RoleNotFoundException.class)
    public ResponseEntity<Object> handleRoleNotFoundException(RoleNotFoundException ex, WebRequest request) {
        log.error(ex.getMessage(), ex);
        return handleCustomException(ex, request, INTERNAL_SERVER_ERROR_MESSAGE, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<Object> handleCustomException(Exception ex, WebRequest request, HttpStatusCode status) {
        ProblemDetail body = createProblemDetail(ex, status, ex.getMessage(), null, null, request);
        return handleExceptionInternal(ex, body, HttpHeaders.EMPTY, status, request);
    }

    private ResponseEntity<Object> handleCustomException(
            Exception ex, WebRequest request, String message, HttpStatusCode status) {
        ProblemDetail body = createProblemDetail(ex, status, message, null, null, request);
        return handleExceptionInternal(ex, body, HttpHeaders.EMPTY, status, request);
    }
}
