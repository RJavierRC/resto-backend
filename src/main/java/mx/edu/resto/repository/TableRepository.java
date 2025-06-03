package mx.edu.resto.repository;

import mx.edu.resto.domain.table.TableResto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.UUID;

public interface TableRepository extends JpaRepository<TableResto, UUID> {
}