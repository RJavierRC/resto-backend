package mx.edu.resto.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.resto.domain.enums.PaymentType;
import mx.edu.resto.dto.*;
import mx.edu.resto.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/waiter")
@RequiredArgsConstructor
public class WaiterController {

    private final OrderService orderService;

    /* Listado de mesas */
    @GetMapping("/tables")
    public List<TableDTO> tables() {
        return orderService.getTables();
    }

    /* Abrir mesa */
    @PostMapping("/tables/{tableId}/open")
    public UUID open(@PathVariable UUID tableId,
                     @AuthenticationPrincipal String waiter) {
        return orderService.openTable(tableId, waiter);
    }

    /* Añadir producto */
    @PostMapping("/orders/{orderId}/items")
    public OrderDTO addItem(@PathVariable UUID orderId,
                            @RequestParam UUID productId,
                            @RequestParam Integer qty) {
        return orderService.addItem(orderId, productId, qty);
    }

    /* Cerrar orden */
    @PostMapping("/orders/{orderId}/close")
    public OrderDTO close(@PathVariable UUID orderId,
                          @RequestParam BigDecimal tip,
                          @RequestParam PaymentType paymentType) {
        return orderService.closeOrder(orderId, tip, paymentType);
    }
    @PostMapping("/tables/{tableId}/reset")
    public TableDTO resetTable(@PathVariable UUID tableId) {
    return orderService.resetTable(tableId);
    }
}
