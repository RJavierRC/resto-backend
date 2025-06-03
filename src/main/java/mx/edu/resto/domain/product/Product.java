package mx.edu.resto.domain.product;
import jakarta.persistence.*; import lombok.*;
import mx.edu.resto.domain.enums.ProductCategory;
import org.hibernate.annotations.UuidGenerator;
import java.math.BigDecimal; import java.util.UUID;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name="products")
public class Product {
    @Id @GeneratedValue @UuidGenerator          private UUID id;
    @Column(nullable=false)                     private String name;
    @Enumerated(EnumType.STRING)                private ProductCategory category;
    @Column(nullable=false)                     private BigDecimal price;
    @Column(nullable=false)                     private boolean active = true;
}
