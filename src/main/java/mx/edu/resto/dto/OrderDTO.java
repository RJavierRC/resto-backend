package mx.edu.resto.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Data
public class OrderDTO {
    private UUID id;
    private LocalDateTime openedAt;
    private LocalDateTime closedAt;
    private BigDecimal tip;
    private String paymentType;
    private List<OrderItemDTO> items;
    private BigDecimal total;
}