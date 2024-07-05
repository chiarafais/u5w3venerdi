package chiarafais.u5w3venerdi.payloads;

import chiarafais.u5w3venerdi.enums.Ruolo;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewUtenteDTO(
        @NotEmpty(message = "È obbligatorio avere un username")
        @Size(min = 3, max = 30, message = "L'username deve essere compreso tra gli 3 e i 30 caratteri")
        String username,
        @NotEmpty(message = "Il nome è obbligatorio")
        @Size(min = 3, max = 30, message = "Il nome deve essere compreso tra i 3 e i 30 caratteri")
        String nome,
        @NotEmpty(message = "Il cognome è obbligatorio")
        @Size(min = 3, max = 30, message = "Il cognome deve essere compreso tra i 3 e i 30 caratteri")
        String cognome,
        @NotEmpty(message = "L'email è obbligatoria")
        @Email(message = "L'email inserita non è valida")
        String email,
        @NotEmpty(message = "La password è obbligatoria")
        String password,
        Ruolo ruolo
) {
}
