package mx.edu.resto.dto;

import jakarta.validation.constraints.Min;

public record TableRequest(
        @Min(1) Integer number
) {}
