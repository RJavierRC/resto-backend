package mx.edu.resto.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.resto.domain.enums.PaymentType;
import mx.edu.resto.dto.OrderDTO;
import mx.edu.resto.dto.TableDTO;
import mx.edu.resto.service.OrderService;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/waiter")
@RequiredArgsConstructor
public class WaiterController {

    private final OrderService orderService;

    /* ---------- LISTA DE MESAS ---------- */
    @GetMapping("/tables")
    public List<TableDTO> tables() {
        return orderService.getTables();                 // cada TableDTO lleva orderId
    }

    /* ---------- ABRIR MESA -------------- */
    @PostMapping("/tables/{tableId}/open")
    public OrderDTO openTable(@PathVariable UUID tableId,
                              @AuthenticationPrincipal String waiterUsername) {
        // crea la orden y devuelve DTO con su id
        return orderService.openTable(tableId, waiterUsername);
    }

    /* ---------- AÑADIR PRODUCTO --------- */
    @PostMapping("/orders/{orderId}/items")
    public OrderDTO addItem(@PathVariable UUID orderId,
                            @RequestParam UUID productId,
                            @RequestParam Integer qty) {
        return orderService.addItem(orderId, productId, qty);
    }

    /* ---------- CERRAR ORDEN ------------ */
    @PostMapping("/orders/{orderId}/close")
    public TableDTO closeOrder(@PathVariable UUID orderId,
                               @RequestParam BigDecimal tip,
                               @RequestParam PaymentType paymentType) {
        // cierra la orden y devuelve el estado final de la mesa
        return orderService.closeOrder(orderId, tip, paymentType);
    }

    /* ---------- REINICIAR MESA (debug) -- */
    @PostMapping("/tables/{tableId}/reset")
    public TableDTO resetTable(@PathVariable UUID tableId) {
        return orderService.resetTable(tableId);
    }
}
