package mx.edu.resto.controller;

import lombok.RequiredArgsConstructor;
import mx.edu.resto.config.JwtUtil;
import mx.edu.resto.domain.user.User;
import mx.edu.resto.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserRepository userRepo;
    private final PasswordEncoder encoder;
    private final JwtUtil jwt;

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> body) {
        String username = body.get("username");
        String password = body.get("password");

        User user = userRepo.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!encoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid credentials");
        }

        String token = jwt.generateToken(user.getUsername(), user.getRole().name());
        return Map.of("token", token, "role", user.getRole().name());
    }
}
