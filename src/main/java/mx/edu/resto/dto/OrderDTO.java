package mx.edu.resto.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

public record OrderDTO(
        UUID id,
        LocalDateTime openedAt,
        LocalDateTime closedAt,
        BigDecimal tip,
        String paymentType,
        List<OrderItemDTO> items,
        BigDecimal total
) {}
