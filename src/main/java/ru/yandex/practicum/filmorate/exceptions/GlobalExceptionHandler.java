package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Collections;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final HttpStatus STATUS_NOT_FOUND = HttpStatus.NOT_FOUND;
    private static final HttpStatus STATUS_INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;
    private static final HttpStatus STATUS_BAD_REQUEST = HttpStatus.BAD_REQUEST;

    private static final String VALIDATION_ERROR_LOG = "Validation error: {}";
    private static final String NOT_FOUND_ERROR_LOG = "Not found error: {}";
    private static final String UNEXPECTED_ERROR_LOG = "Unexpected error occurred: ";

    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<Map<String, String>> handleCustomValidationExceptions(ValidationException ex) {
        log.error(VALIDATION_ERROR_LOG, ex.getMessage());
        Map<String, String> errorResponse = Collections.singletonMap("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, STATUS_BAD_REQUEST);
    }

    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<Map<String, String>> handleNotFoundExceptions(NoSuchElementException ex) {
        log.error(NOT_FOUND_ERROR_LOG, ex.getMessage());
        Map<String, String> errorResponse = Collections.singletonMap("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, STATUS_NOT_FOUND);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, String>> handleIllegalArgumentException(IllegalArgumentException ex) {
        log.error("Illegal argument error: {}", ex.getMessage());
        Map<String, String> errorResponse = Collections.singletonMap("error", ex.getMessage());
        return new ResponseEntity<>(errorResponse, STATUS_INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, String>> handleAllExceptions(Exception ex) {
        log.error(UNEXPECTED_ERROR_LOG, ex);
        Map<String, String> errorResponse = Collections.singletonMap("error", "An unexpected error occurred.");
        return new ResponseEntity<>(errorResponse, STATUS_INTERNAL_SERVER_ERROR);
    }
}








