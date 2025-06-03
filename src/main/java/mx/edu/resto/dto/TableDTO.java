package mx.edu.resto.dto;

import java.util.UUID;

public record TableDTO(
        UUID id,
        Integer number,
        String status,
        UUID currentOrderId      // null si la mesa está libre
) {}
