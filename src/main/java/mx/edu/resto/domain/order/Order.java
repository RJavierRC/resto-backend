package mx.edu.resto.domain.order;
import jakarta.persistence.*; import lombok.*;
import mx.edu.resto.domain.enums.PaymentType;
import mx.edu.resto.domain.table.TableResto;
import mx.edu.resto.domain.user.User;
import org.hibernate.annotations.UuidGenerator;
import java.math.BigDecimal; import java.time.LocalDateTime;
import java.util.*;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name="orders")
public class Order {
    @Id @GeneratedValue @UuidGenerator          private UUID id;
    private LocalDateTime openedAt = LocalDateTime.now();
    private LocalDateTime closedAt;
    private BigDecimal tip = BigDecimal.ZERO;
    @Enumerated(EnumType.STRING)                private PaymentType paymentType;
    @ManyToOne(optional=false)                  private TableResto tableResto;
    @ManyToOne(optional=false)                  private User waiter;
    @OneToMany(mappedBy="order", cascade=CascadeType.ALL, orphanRemoval=true)
    private List<OrderItem> items = new ArrayList<>();
}
