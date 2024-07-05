package chiarafais.u5w3venerdi.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
public class Evento {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    private String titolo;
    private String descrizione;
    private LocalDate data;
    private String location;
    private int capienza;

    public Evento(String titolo, String descrizione, LocalDate data, String location, int capienza) {
        this.titolo=titolo;
        this.descrizione=descrizione;
        this.data=data;
        this.location=location;
        this.capienza=capienza;
    }
}