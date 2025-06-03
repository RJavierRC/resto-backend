package mx.edu.resto.repository;

import mx.edu.resto.domain.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface OrderRepository extends JpaRepository<Order, UUID> {

    /* orden abierta de una mesa */
    Optional<Order> findByTableRestoIdAndClosedAtIsNull(UUID tableId);
}
