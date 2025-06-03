package mx.edu.resto.dto;

import jakarta.validation.constraints.*;
import mx.edu.resto.domain.enums.ProductCategory;

import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank  String name,
        @NotNull   ProductCategory category,
        @DecimalMin(value = "0.0", inclusive = false) BigDecimal price,
        boolean active
) {}
