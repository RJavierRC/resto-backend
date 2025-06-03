package mx.edu.resto.config;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<?> badRequest(MethodArgumentNotValidException ex) {
    String msg = ex.getFieldErrors().get(0).getDefaultMessage();
        return ResponseEntity.badRequest().body(new ErrorDTO(msg));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<?> generic(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDTO(ex.getMessage()));
    }

    record ErrorDTO(String error) {}
}
