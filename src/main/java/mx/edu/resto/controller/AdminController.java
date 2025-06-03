package mx.edu.resto.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mx.edu.resto.domain.enums.TableStatus;
import mx.edu.resto.domain.user.User;
import mx.edu.resto.dto.*;
import mx.edu.resto.repository.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import java.util.List;
import java.util.UUID;

@SecurityRequirement(name = "bearerAuth")
@RestController
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ProductRepository productRepo;
    private final UserRepository userRepo;
    private final TableRepository tableRepo;
    private final BCryptPasswordEncoder encoder;

    /* ---------- Productos ---------- */

    @GetMapping("/products")
    public List<?> allProducts() {
        return productRepo.findAll();
    }

    @PostMapping("/products")
    public Object createProduct(@Valid @RequestBody ProductRequest req) {
        var p = productRepo.save(new mx.edu.resto.domain.product.Product(
                null, req.name(), req.category(), req.price(), req.active()
        ));
        return p;
    }

    @PutMapping("/products/{id}")
    public Object updateProduct(@PathVariable UUID id,
                                @Valid @RequestBody ProductRequest req) {
        var p = productRepo.findById(id).orElseThrow();
        p.setName(req.name());
        p.setCategory(req.category());
        p.setPrice(req.price());
        p.setActive(req.active());
        return productRepo.save(p);
    }

    @DeleteMapping("/products/{id}")
    public void deleteProduct(@PathVariable UUID id) {
        productRepo.deleteById(id);
    }

    /* ---------- Meseros / Usuarios ---------- */

    @GetMapping("/users")
    public List<User> allUsers() { return userRepo.findAll(); }

    @PostMapping("/users")
public User createUser(@Valid @RequestBody UserRequest req) {
    return userRepo.save(new User(
            null,
            req.username(),
            encoder.encode(req.password()), // Esto está bien
            mx.edu.resto.domain.enums.UserRole.valueOf(req.role())
    ));
}

    @PutMapping("/users/{id}")
public User updateUser(@PathVariable UUID id,
                       @Valid @RequestBody UserRequest req) {
    var u = userRepo.findById(id).orElseThrow();
    u.setUsername(req.username());
    if (req.password() != null && !req.password().isBlank()) {
        u.setPasswordHash(encoder.encode(req.password())); // Cambiado a setPasswordHash
    }
    u.setRole(mx.edu.resto.domain.enums.UserRole.valueOf(req.role()));
    return userRepo.save(u);
}
    @DeleteMapping("/users/{id}")
    public void deleteUser(@PathVariable UUID id) {
        userRepo.deleteById(id);
    }

    /* ---------- Mesas ---------- */

    @GetMapping("/tables")
    public List<?> allTables() { return tableRepo.findAll(); }

    @PostMapping("/tables")
    public Object createTable(@Valid @RequestBody TableRequest req) {
        return tableRepo.save(new mx.edu.resto.domain.table.TableResto(
                null, req.number(), TableStatus.FREE
        ));
    }

    @PutMapping("/tables/{id}")
    public Object updateTable(@PathVariable UUID id,
                              @Valid @RequestBody TableRequest req) {
        var t = tableRepo.findById(id).orElseThrow();
        t.setNumber(req.number());
        return tableRepo.save(t);
    }

    @DeleteMapping("/tables/{id}")
    public void deleteTable(@PathVariable UUID id) {
        tableRepo.deleteById(id);
    }
}
