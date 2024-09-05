package ru.yandex.practicum.filmorate.exceptions;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    // HTTP Statuses
    private static final HttpStatus STATUS_BAD_REQUEST = HttpStatus.BAD_REQUEST;
    private static final HttpStatus STATUS_INTERNAL_SERVER_ERROR = HttpStatus.INTERNAL_SERVER_ERROR;

    // Error Messages
    private static final String VALIDATION_ERROR_LOG = "Validation error: {} - {}";
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





