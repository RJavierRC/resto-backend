package mx.edu.resto.bootstrap;

import lombok.RequiredArgsConstructor;
import mx.edu.resto.domain.enums.*;
import mx.edu.resto.domain.product.Product;
import mx.edu.resto.domain.table.TableResto;
import mx.edu.resto.domain.user.User;
import mx.edu.resto.repository.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.IntStream;

@Configuration
@RequiredArgsConstructor
public class DataSeeder {

    private final UserRepository userRepo;
    private final TableRepository tableRepo;
    private final ProductRepository productRepo;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Bean
    CommandLineRunner init() {
        return args -> {

            /* ── Usuarios ───────────────────────────── */
            if (userRepo.count() == 0) {
                userRepo.saveAll(List.of(
                        new User(null, "admin", encoder.encode("admin123"), UserRole.ADMIN),
                        new User(null, "juan",  encoder.encode("waiter1"),  UserRole.WAITER),
                        new User(null, "maria", encoder.encode("waiter2"),  UserRole.WAITER)
                ));
            }

            /* ── Mesas ─────────────────────────────── */
            if (tableRepo.count() == 0) {
                IntStream.rangeClosed(1, 6)
                         .forEach(n ->
                             tableRepo.save(new TableResto(null, n, TableStatus.FREE)));
            }

            /* ── Productos ─────────────────────────── */
            if (productRepo.count() == 0) {

                /* Comidas */
                List<Product> comidas = List.of(
                        new Product(null, "Hamburguesa clásica", ProductCategory.FOOD, BigDecimal.valueOf(90), true),
                        new Product(null, "Tacos al pastor (orden)", ProductCategory.FOOD, BigDecimal.valueOf(70), true),
                        new Product(null, "Ensalada César", ProductCategory.FOOD, BigDecimal.valueOf(65), true)
                );

                /* Bebidas */
                List<Product> bebidas = List.of(
                        new Product(null, "Coca-Cola 355 ml", ProductCategory.DRINK, BigDecimal.valueOf(25), true),
                        new Product(null, "Sprite 355 ml",    ProductCategory.DRINK, BigDecimal.valueOf(25), true),
                        new Product(null, "Agua natural 600 ml", ProductCategory.DRINK, BigDecimal.valueOf(18), true),
                        new Product(null, "Café americano",   ProductCategory.DRINK, BigDecimal.valueOf(22), true),
                        new Product(null, "Cerveza lager",    ProductCategory.DRINK, BigDecimal.valueOf(40), true)
                );

                /* Postres */
                List<Product> postres = List.of(
                        new Product(null, "Flan napolitano",  ProductCategory.DESSERT, BigDecimal.valueOf(45), true),
                        new Product(null, "Pay de queso",     ProductCategory.DESSERT, BigDecimal.valueOf(50), true),
                        new Product(null, "Helado vainilla",  ProductCategory.DESSERT, BigDecimal.valueOf(38), true)
                );

                productRepo.saveAll(comidas);
                productRepo.saveAll(bebidas);
                productRepo.saveAll(postres);
            }
        };
    }
}
