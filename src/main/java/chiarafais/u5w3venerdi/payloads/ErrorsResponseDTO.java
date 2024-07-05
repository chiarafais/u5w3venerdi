package chiarafais.u5w3venerdi.payloads;

import java.time.LocalDateTime;

public record ErrorsResponseDTO(String message, LocalDateTime timestamp) {
}
