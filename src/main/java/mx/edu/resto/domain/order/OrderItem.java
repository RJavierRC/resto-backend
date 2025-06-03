package mx.edu.resto.domain.order;
import jakarta.persistence.*; import lombok.*;
import mx.edu.resto.domain.product.Product;
import org.hibernate.annotations.UuidGenerator;
import java.math.BigDecimal; import java.util.UUID;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name="order_items")
public class OrderItem {
    @Id @GeneratedValue @UuidGenerator          private UUID id;
    private Integer qty;
    private BigDecimal priceSnapshot;
    @ManyToOne(optional=false)                  private Product product;
    @ManyToOne(optional=false)                  private Order order;
}
