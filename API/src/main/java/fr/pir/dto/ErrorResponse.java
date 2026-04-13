package fr.pir.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Standardized error response DTO for all error scenarios
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ErrorResponse {

    private String status;
    private String message;
    private int code;
    private LocalDateTime timestamp;
    private List<FieldError> errors;

    /**
     * Individual field validation error
     */
    @Data
    @NoArgsConstructor
    @AllArgsConstructor
    public static class FieldError {
        private String field;
        private String message;
    }
}

