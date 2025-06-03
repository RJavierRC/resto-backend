package mx.edu.resto.service;

import lombok.RequiredArgsConstructor;
import mx.edu.resto.domain.enums.PaymentType;
import mx.edu.resto.domain.enums.TableStatus;
import mx.edu.resto.domain.order.Order;
import mx.edu.resto.domain.order.OrderItem;
import mx.edu.resto.domain.product.Product;
import mx.edu.resto.domain.table.TableResto;
import mx.edu.resto.domain.user.User;
import mx.edu.resto.dto.*;
import mx.edu.resto.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final TableRepository tableRepo;
    private final OrderRepository orderRepo;
    private final OrderItemRepository itemRepo;
    private final ProductRepository productRepo;
    private final UserRepository userRepo;

    /* ---------- Helpers de mapeo ---------- */
    private TableDTO toTableDTO(TableResto t) {
        UUID orderId = orderRepo.findByTableRestoIdAndClosedAtIsNull(t.getId())
                .map(Order::getId).orElse(null);
        return new TableDTO(t.getId(), t.getNumber(), t.getStatus().name(), orderId);
    }

    private OrderDTO toOrderDTO(Order o) {
        BigDecimal total = o.getItems().stream()
                .map(i -> i.getPriceSnapshot().multiply(BigDecimal.valueOf(i.getQty())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return new OrderDTO(
                o.getId(), o.getOpenedAt(), o.getClosedAt(),
                o.getTip(), o.getPaymentType() == null ? null : o.getPaymentType().name(),
                o.getItems().stream().map(this::toItemDTO).toList(),
                total.add(o.getTip())
        );
    }

    private OrderItemDTO toItemDTO(OrderItem i) {
        return new OrderItemDTO(i.getId(), i.getProduct().getName(), i.getQty(), i.getPriceSnapshot());
    }

    /* ---------- API del servicio ---------- */

    public List<TableDTO> getTables() {
        return tableRepo.findAll().stream().map(this::toTableDTO).toList();
    }

    @Transactional
    public UUID openTable(UUID tableId, String waiterUsername) {
        TableResto table = tableRepo.findById(tableId)
                .orElseThrow(() -> new RuntimeException("Mesa no encontrada"));
        if (table.getStatus() != TableStatus.FREE) throw new RuntimeException("Mesa ocupada");

        User waiter = userRepo.findByUsername(waiterUsername)
                .orElseThrow(() -> new RuntimeException("Mesero no existe"));

        Order order = new Order();
        order.setTableResto(table);
        order.setWaiter(waiter);
        orderRepo.save(order);

        table.setStatus(TableStatus.BUSY);
        tableRepo.save(table);

        return order.getId();
    }

    @Transactional
    public OrderDTO addItem(UUID orderId, UUID productId, Integer qty) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no existe"));
        if (order.getClosedAt() != null) throw new RuntimeException("Orden cerrada");

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
    public OrderDTO closeOrder(UUID orderId, BigDecimal tip, PaymentType paymentType) {
        Order order = orderRepo.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Orden no existe"));
        order.setTip(tip);
        order.setPaymentType(paymentType);
        order.setClosedAt(java.time.LocalDateTime.now());
        orderRepo.save(order);

        TableResto t = order.getTableResto();
        t.setStatus(TableStatus.CLOSED);
        tableRepo.save(t);

        return toOrderDTO(order);
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
