// Ubicación: src/main/java/mx/edu/resto/dto/TableDTO.java
package mx.edu.resto.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class TableDTO {
    private UUID id;
    private int   number;
    private String status; // FREE, BUSY, CLOSED 
    private UUID orderId;  // null si está libre / cerrada 
}