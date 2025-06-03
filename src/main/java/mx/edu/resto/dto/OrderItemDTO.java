package mx.edu.resto.dto;

import java.math.BigDecimal;
import java.util.UUID;

public record OrderItemDTO(
        UUID id,
        String productName,
        Integer qty,
        BigDecimal priceSnapshot
) {}
