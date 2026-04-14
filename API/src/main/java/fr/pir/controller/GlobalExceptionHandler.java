package fr.pir.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import fr.pir.dto.ErrorResponse;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Global exception handler for all controllers
 * Provides consistent error response format across the API
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger L = LogManager.getLogger(GlobalExceptionHandler.class);

    /**
     * Handle validation errors from @Valid annotation
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> handleValidationExceptions(MethodArgumentNotValidException ex) {
        L.warn("Validation error occurred: {}", ex.getMessage());

        List<ErrorResponse.FieldError> errors = ex.getBindingResult().getAllErrors()
            .stream()
            .map(error -> {
                String field = error instanceof FieldError ? ((FieldError) error).getField() : error.getObjectName();
                return new ErrorResponse.FieldError(
                    field,
                    error.getDefaultMessage()
                );
            })
            .collect(Collectors.toList());

        ErrorResponse response = ErrorResponse.builder()
            .status("error")
            .message("Validation failed")
            .code(400)
            .timestamp(LocalDateTime.now())
            .errors(errors)
            .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    /**
     * Handle general exceptions
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneralException(Exception ex) {
        L.error("Unexpected error: {} - {}", ex.getClass().getName(), ex.getMessage(), ex);

        ErrorResponse response = ErrorResponse.builder()
            .status("error")
            .message("An unexpected error occurred: " + ex.getMessage())
            .code(500)
            .timestamp(LocalDateTime.now())
            .build();

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}

