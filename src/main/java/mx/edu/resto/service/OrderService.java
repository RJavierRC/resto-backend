package mx.edu.resto.service;

import lombok.RequiredArgsConstructor;
import mx.edu.resto.domain.enums.PaymentType;
import mx.edu.resto.domain.enums.TableStatus;
import mx.edu.resto.domain.order.Order;
import mx.edu.resto.domain.order.OrderItem;
import mx.edu.resto.domain.product.Product;
import mx.edu.resto.domain.table.TableResto;
import mx.edu.resto.domain.user.User;
import mx.edu.resto.dto.OrderDTO;
import mx.edu.resto.dto.OrderItemDTO;
import mx.edu.resto.dto.TableDTO;
import mx.edu.resto.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final TableRepository tableRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    /* ---------- Helpers de mapeo (Sin cambios, ya estaban correctos) ---------- */
    private TableDTO toTableDTO(TableResto t) {
        // Esta implementación ya busca correctamente el ID de la orden activa.
        UUID orderId = orderRepo.findByTableRestoIdAndClosedAtIsNull(t.getId())
                .map(Order::getId).orElse(null);
        // La firma de tu DTO puede variar, esto asume que tienes un constructor que acepta estos campos.
        // Si no, ajusta la creación del DTO aquí (ej. usando setters).
        TableDTO dto = new TableDTO();
        dto.setId(t.getId());
        dto.setNumber(t.getNumber());
        dto.setStatus(t.getStatus().name());
        dto.setOrderId(orderId);
        return dto;
    }

    private OrderDTO toOrderDTO(Order o) {
        BigDecimal total = o.getItems().stream()
                .map(i -> i.getPriceSnapshot().multiply(BigDecimal.valueOf(i.getQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        // La firma de tu DTO puede variar, esto asume que tienes un constructor que acepta estos campos.
        // Si no, ajusta la creación del DTO aquí (ej. usando setters).
        OrderDTO dto = new OrderDTO();
        dto.setId(o.getId());
        // Asumiendo que tu OrderDTO tiene un campo para el total sin propina.
        // Basado en la instrucción original, el DTO mínimo solo necesitaba id y total.
        dto.setTotal(total); 
        return dto;
    }

    private OrderItemDTO toItemDTO(OrderItem i) {
        // Tu implementación original de este helper es más compleja de lo necesario para el DTO mínimo,
        // pero la dejamos por si la usas en otros lugares.
        return new OrderItemDTO(i.getId(), i.getProduct().getName(), i.getQty(), i.getPriceSnapshot());
    }

    /* ---------- API del servicio (Con cambios) ---------- */

    public List<TableDTO> getTables() {
        return tableRepo.findAll().stream().map(this::toTableDTO).toList();
    }

    @Transactional
    public OrderDTO openTable(UUID tableId, String waiterUsername) {
        TableResto table = tableRepo.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        if (table.getStatus() != TableStatus.FREE) {
            throw new RuntimeException("Mesa ocupada");
        }

        User waiter = userRepo.findByUsername(waiterUsername)
                .orElseThrow(() -> new RuntimeException("Mesero no existe"));
        
        Order order = new Order();
        order.setTableResto(table);
        order.setWaiter(waiter);
        orderRepo.save(order);

        table.setStatus(TableStatus.BUSY);
        tableRepo.save(table);

        // --- CAMBIO APLICADO ---
        // En lugar de devolver solo el ID, devolvemos el DTO completo.
        return toOrderDTO(order);
    }

    @Transactional
    public OrderDTO addItem(UUID orderId, UUID productId, Integer qty) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no existe"));
        if (order.getClosedAt() != null) {
            throw new RuntimeException("Orden cerrada");
        }

        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new RuntimeException("Producto no existe"));
        
        OrderItem item = new OrderItem();
        item.setOrder(order);
        item.setProduct(product);
        item.setQty(qty);
        item.setPriceSnapshot(product.getPrice());
        itemRepo.save(item);

        order.getItems().add(item);
        return toOrderDTO(order);
    }

    @Transactional
    public TableDTO closeOrder(UUID orderId, BigDecimal tip, PaymentType paymentType) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no existe"));
        
        order.setTip(tip);
        order.setPaymentType(paymentType);
        order.setClosedAt(java.time.LocalDateTime.now());
        orderRepo.save(order);

        TableResto t = order.getTableResto();
        t.setStatus(TableStatus.CLOSED);
        tableRepo.save(t);

        // --- CAMBIO APLICADO ---
        // En lugar de devolver el DTO de la orden, devolvemos el DTO
        // de la mesa con su estado actualizado a CLOSED.
        return toTableDTO(t);
    }

    @Transactional
    public TableDTO resetTable(UUID tableId) {
        TableResto t = tableRepo.findById(tableId)
            .orElseThrow(() -> new RuntimeException("Mesa no existe"));
        t.setStatus(TableStatus.FREE);
        tableRepo.save(t);
        return toTableDTO(t);
    }
}