package mx.edu.resto.repository;

import mx.edu.resto.domain.order.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface OrderItemRepository extends JpaRepository<OrderItem, UUID> {
}