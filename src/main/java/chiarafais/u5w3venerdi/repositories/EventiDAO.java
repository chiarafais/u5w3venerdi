package chiarafais.u5w3venerdi.repositories;

import chiarafais.u5w3venerdi.entities.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface EventiDAO extends JpaRepository<Evento, Integer> {
    boolean existsByTitolo(String titolo);
    Optional<Evento> findByTitolo(String titolo);
}