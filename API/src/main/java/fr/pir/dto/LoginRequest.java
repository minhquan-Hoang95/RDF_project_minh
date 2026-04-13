package fr.pir.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for login requests - validates email and password format
 */
@Data
@NoArgsConstructor
public class LoginRequest {

    @Email(message = "Email must be valid")
    @NotBlank(message = "Email is required")
    private String email;

    @Size(min = 8, max = 255, message = "Password must be at least 8 characters")
    @NotBlank(message = "Password is required")
    private String password;
}

