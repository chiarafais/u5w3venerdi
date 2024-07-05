package chiarafais.u5w3venerdi.services;


import chiarafais.u5w3venerdi.entities.Evento;
import chiarafais.u5w3venerdi.entities.Utente;
import chiarafais.u5w3venerdi.exceptions.BadRequestException;
import chiarafais.u5w3venerdi.exceptions.NotFoundException;
import chiarafais.u5w3venerdi.payloads.NewUtenteDTO;
import chiarafais.u5w3venerdi.repositories.EventiDAO;
import chiarafais.u5w3venerdi.repositories.UtentiDAO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UtentiService {

    @Autowired
    private UtentiDAO utentiDAO;

    @Autowired
    private EventiDAO eventiDAO;

    @Autowired
    private PasswordEncoder bcrypt;


    public UtentiService(UtentiDAO utentiDAO) {
        this.utentiDAO = utentiDAO;
    }

    public List<Utente> getUtentiList() {
        return utentiDAO.findAll();
    }



    public Utente saveUtente(NewUtenteDTO newUtenteDTO) {

        if (utentiDAO.existsByEmail(newUtenteDTO.email())) {
            throw new BadRequestException("L'email " + newUtenteDTO.email() + " è già in uso, quindi l'utente ha già un account! Contatta l'assistenza se hai dimenticato la tua password");
        }

        Utente utente = new Utente(newUtenteDTO.username(), newUtenteDTO.nome(), newUtenteDTO.cognome(), newUtenteDTO.email(), bcrypt.encode(newUtenteDTO.password()), newUtenteDTO.ruolo());
//        System.out.println(utente);
        return utentiDAO.save(utente);
    }


    public Utente findById(int id) {
        return utentiDAO.findById(id)
                .orElseThrow(() -> new NotFoundException(id));
    }

    public Utente findByIdAndUpdate(int id, Utente updatedUtente) {
        Utente found = findById(id);
        found.setNome(updatedUtente.getNome());
        found.setCognome(updatedUtente.getCognome());
        return utentiDAO.save(found);
    }

    public void findByIdAndDelete(int utenteId) {
        utentiDAO.deleteById(utenteId);
    }


    public Page<Utente> getUtenti(int page, int size, String sortBy){
        if(size > 70) size = 70;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.utentiDAO.findAll(pageable);
    }

    public Utente findByEmail(String email) {
        return utentiDAO.findByEmail(email).orElseThrow(() -> new NotFoundException("Utente con email " + email + " non trovato!"));
    }


    @Transactional(readOnly = true)
    public List<Evento> getPrenotazioniUtente(int utenteId) {
        Optional<Utente> optionalUtente = utentiDAO.findById(utenteId);
        if (optionalUtente.isPresent()) {
            Utente utente = optionalUtente.get();
            return utente.getPrenotazioni();
        }
        return Collections.emptyList(); // Utente non trovato o nessuna prenotazione

    }

    @Transactional
    public boolean annullaPrenotazione(int utenteId, int eventoId) {
        Optional<Utente> optionalUtente = utentiDAO.findById(utenteId);
        Optional<Evento> optionalEvento = eventiDAO.findById(eventoId);

        if (optionalUtente.isPresent() && optionalEvento.isPresent()) {
            Utente utente = optionalUtente.get();
            Evento evento = optionalEvento.get();

            // Elimino l'evento dalle prenotazioni dell'utente
            boolean removed = utente.getPrenotazioni().remove(evento);
            if (removed) {
                evento.setCapienza(evento.getCapienza() + 1);
                utentiDAO.save(utente);
                eventiDAO.save(evento);
                return true;
            }
        }
        return false;
    }
}
