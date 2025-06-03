package mx.edu.resto.domain.user;

import jakarta.persistence.*;
import lombok.*;
import mx.edu.resto.domain.enums.UserRole;
import org.hibernate.annotations.UuidGenerator;
import java.util.UUID;

@Entity @Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Table(name = "users")
public class User {
    @Id @GeneratedValue @UuidGenerator          private UUID id;
    @Column(nullable = false, unique = true)    private String username;
    @Column(nullable = false)                   private String passwordHash;
    @Enumerated(EnumType.STRING)                private UserRole role;
}
