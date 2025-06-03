package mx.edu.resto.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.resto.repository.ProductRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductRepository repo;

    @GetMapping("/search")
    public List<?> search(@RequestParam String q,
                          @RequestParam(defaultValue = "0") int page,
                          @RequestParam(defaultValue = "10") int size) {
        return repo.findByNameContainingIgnoreCaseAndActiveTrue(
                q, PageRequest.of(page, size));
    }
}
