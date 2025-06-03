package mx.edu.resto.dto;

import jakarta.validation.constraints.*;

public record UserRequest(
        @NotBlank String username,
        @Size(min = 6) String password,
        @NotBlank String role              // "ADMIN" o "WAITER"
) {}
