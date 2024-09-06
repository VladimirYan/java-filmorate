package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // HTTP Statuses
    private static final HttpStatus STATUS_BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private static final HttpStatus STATUS_NOT_FOUND = HttpStatus.NOT_FOUND;
    private static final HttpStatus STATUS_INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;

    // Error Messages
    private static final String VALIDATION_ERROR_LOG = "Validation error: {} - {}";
    private static final String NOT_FOUND_ERROR_LOG = "Not found error: {}";
    private static final String UNEXPECTED_ERROR_LOG = "Unexpected error occurred: ";

    /**
     * Handles validation exceptions thrown during the request binding process.
     *
     * @param ex MethodArgumentNotValidException
     * @return ResponseEntity with validation errors and BAD_REQUEST status
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = error instanceof org.springframework.validation.FieldError ?
                    ((org.springframework.validation.FieldError) error).getField() : error.getObjectName();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
            log.error(VALIDATION_ERROR_LOG, fieldName, errorMessage);
        });
        return new ResponseEntity<>(errors, STATUS_BAD_REQUEST);
    }

    /**
     * Handles custom validation exceptions.
     *
     * @param ex ValidationException
     * @return ResponseEntity with validation error message and BAD_REQUEST status
     */
    @ExceptionHandler(ValidationException.class)
    public ResponseEntity<String> handleCustomValidationExceptions(ValidationException ex) {
        log.error(VALIDATION_ERROR_LOG, "validation", ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), STATUS_BAD_REQUEST);
    }

    /**
     * Handles not found exceptions.
     *
     * @param ex NoSuchElementException
     * @return ResponseEntity with not found error message and NOT_FOUND status
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNotFoundExceptions(NoSuchElementException ex) {
        log.error(NOT_FOUND_ERROR_LOG, ex.getMessage());
        return new ResponseEntity<>(ex.getMessage(), STATUS_NOT_FOUND);
    }

    /**
     * Handles all other exceptions that may occur during the application's runtime.
     *
     * @param ex Exception
     * @return ResponseEntity with exception message and INTERNAL_SERVER_ERROR status
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleAllExceptions(Exception ex) {
        log.error(UNEXPECTED_ERROR_LOG, ex);
        return new ResponseEntity<>(ex.getMessage(), STATUS_INTERNAL_SERVER_ERROR);
    }
}






