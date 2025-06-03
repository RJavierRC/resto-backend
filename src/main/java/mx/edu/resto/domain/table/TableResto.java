package mx.edu.resto.domain.table;
import jakarta.persistence.*; import lombok.*;
import mx.edu.resto.domain.enums.TableStatus;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name="tables")
public class TableResto {
    @Id @GeneratedValue @UuidGenerator          private UUID id;
    @Column(nullable=false, unique=true)        private Integer number;
    @Enumerated(EnumType.STRING)                private TableStatus status = TableStatus.FREE;
}
