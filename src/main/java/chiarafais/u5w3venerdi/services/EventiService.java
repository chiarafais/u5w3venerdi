package chiarafais.u5w3venerdi.services;

import chiarafais.u5w3venerdi.entities.Evento;
import chiarafais.u5w3venerdi.exceptions.NotFoundException;
import chiarafais.u5w3venerdi.payloads.NewEventoDTO;
import chiarafais.u5w3venerdi.repositories.EventiDAO;
import chiarafais.u5w3venerdi.repositories.UtentiDAO;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventiService {
    @Autowired
    private EventiDAO eventiDAO;

    @Autowired
    private UtentiDAO utentiDAO;

    public EventiService(EventiDAO eventiDAO) {
        this.eventiDAO = eventiDAO;
    }

    public List<Evento> getEventiList() {
        return eventiDAO.findAll();
    }


    public Evento saveEvento(NewEventoDTO newEventoDTO) {
        Evento evento = new Evento(newEventoDTO.titolo(),newEventoDTO.descrizione(),newEventoDTO.data(),newEventoDTO.location(),newEventoDTO.capienza());
//        System.out.println(dispositivo);
        return eventiDAO.save(evento);
    }



    public Evento findById(int id) {
        return eventiDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Evento findByIdAndUpdate(int id, Evento updatedEvento) {
        Evento found = findById(id);
        found.setTitolo(updatedEvento.getTitolo());
        found.setDescrizione(updatedEvento.getDescrizione());
        found.setData(updatedEvento.getData());
        found.setLocation(updatedEvento.getLocation());
        found.setCapienza(updatedEvento.getCapienza());
        return eventiDAO.save(found);
    }

    public void findByIdAndDelete(int dispositivoId) {
        eventiDAO.deleteById(dispositivoId);
    }


    public Page<Evento> getDispositivi(int page, int size, String sortBy){
        if(size > 50) size = 50;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.eventiDAO.findAll(pageable);
    }

    @Transactional
    public boolean prenotaPosto(int eventoId) {
        Optional<Evento> optionalEvento = eventiDAO.findById(eventoId);
        if (optionalEvento.isPresent()) {
            Evento evento = optionalEvento.get();
            if (evento.getCapienza() > 0) {
                evento.setCapienza(evento.getCapienza() - 1);
                eventiDAO.save(evento);
                return true; // Prenotazione riuscita
            }
        }
        return false; // Prenotazione non riuscita
    }
}

