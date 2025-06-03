package mx.edu.resto.repository;

import mx.edu.resto.domain.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    List<Product> findByNameContainingIgnoreCaseAndActiveTrue(String name, Pageable pageable);
}