package chiarafais.u5w3venerdi.payloads;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
public class ErrorsPayload {
    private String message;
    private LocalDateTime timestamp;
}