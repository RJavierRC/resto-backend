// src/main/java/mx/edu/resto/RestoApiApplication.java
package mx.edu.resto;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SecurityScheme(                         // ← NUEVO
        name = "bearerAuth",             // nombre que usará Swagger
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER
)
@SpringBootApplication
public class RestoApiApplication {
    public static void main(String[] args) {
        SpringApplication.run(RestoApiApplication.class, args);
    }
}
