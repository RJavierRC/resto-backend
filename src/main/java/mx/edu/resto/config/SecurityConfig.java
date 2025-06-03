package mx.edu.resto.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtUtil jwt;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/v3/**", "/swagger-ui.html", "/hello").permitAll()
                     .requestMatchers("/admin/**").hasRole("ADMIN")   // ← nueva línea
                .anyRequest().authenticated())
            .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    @Bean
WebSecurityCustomizer swaggerCustomizer() {
    return web -> web.ignoring().requestMatchers(
            "/swagger-ui.html",
            "/swagger-ui/**",
            "/v3/api-docs/**",
            "/swagger-resources/**");
}


    @Bean
    public BCryptPasswordEncoder passwordEncoder() { return new BCryptPasswordEncoder(); }

    @Bean
    public OncePerRequestFilter jwtFilter() {
        return new OncePerRequestFilter() {
            @Override
            protected void doFilterInternal(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain)
                                            throws ServletException, IOException {
                String auth = req.getHeader("Authorization");
                if (auth != null && auth.startsWith("Bearer ")) {
                    String token = auth.substring(7);
                    try {
                        var claims = jwt.validate(token);   // ahora es Claims, no Jws<Claims>

                       var authToken = new UsernamePasswordAuthenticationToken(
        claims.getSubject(),
        null,
        List.of(new SimpleGrantedAuthority("ROLE_" + claims.get("role", String.class))));
SecurityContextHolder.getContext().setAuthentication(authToken);

                    } catch (Exception ignored) { }
                }
                chain.doFilter(req, res);
            }
        };
    }
    
}
