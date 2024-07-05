package chiarafais.u5w3venerdi.controllers;

import chiarafais.u5w3venerdi.entities.Evento;
import chiarafais.u5w3venerdi.exceptions.BadRequestException;
import chiarafais.u5w3venerdi.payloads.NewEventoDTO;
import chiarafais.u5w3venerdi.payloads.NewEventoRespDTO;
import chiarafais.u5w3venerdi.services.EventiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/eventi")
public class EventiController {
    @Autowired
    private EventiService eventiService;

    //    1. POST http://localhost:3001/eventi (+ body) (+authorization bear token da organizzatore)
    @PostMapping
    @PreAuthorize("hasAuthority('ORGANIZZATORE')") //solo un organizzatore può creare eventi
    @ResponseStatus(HttpStatus.CREATED)
    public NewEventoRespDTO saveEvento(@RequestBody @Validated NewEventoDTO body, BindingResult validation){

        if(validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }
//        System.out.println(body);
        return new NewEventoRespDTO(this.eventiService.saveEvento(body).getId());}




    // 2. GET http://localhost:3001/eventi/{{eventoId}}
    @GetMapping("/{eventoId}")
    private Evento findEventoById(@PathVariable int eventoId){
        return this.eventiService.findById(eventoId);
    }

    //    3. GET http://localhost:3001/eventi
    @GetMapping
    public List<Evento> getAllEvento(){
        return this.eventiService.getEventiList();
    }

    //    3.1 Paginazione e ordinamento http://localhost:3001/eventi/page
    @GetMapping("/page")
    public Page<Evento> getAllEventi(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "1") int size,
                                     @RequestParam(defaultValue = "id") String sortBy) {
        return this.eventiService.getDispositivi(page, size, sortBy);
    }

    // 4. PUT http://localhost:3001/eventi/{{eventoId}} (+ body) (+authorization bear token da organizzatore)
    @PutMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')") //solo un organizzatore può modificare eventi
    public Evento findByIdAndUpdate(@PathVariable int eventoId, @RequestBody Evento body){
        return this.eventiService.findByIdAndUpdate(eventoId, body);
    }



    // 5. DELETE http://localhost:3001/eventi/{eventoId} (+authorization bear token da organizzatore)
    @DeleteMapping("/{eventoId}")
    @PreAuthorize("hasAuthority('ORGANIZZATORE')") //solo un organizzatore può modificare eventi
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteEventoById(@PathVariable int eventoId) {
        this.eventiService.findByIdAndDelete(eventoId);
    }

    //     PRENOTA
//     POST http://localhost:3001/eventi/{eventoId}/prenota (+authorization bear token da partecipante)
    @PostMapping("/{eventoId}/prenota")
    @PreAuthorize("hasAuthority('PARTECIPANTE')") //solo un utente di tipo partecipante può prenotare un evento
    public ResponseEntity<String> prenotaPosto(@PathVariable int eventoId) {
        boolean prenotazioneRiuscita = eventiService.prenotaPosto(eventoId);
        if (prenotazioneRiuscita) {
            return ResponseEntity.ok("Prenotazione avvenuta con successo!");
        } else {
            return ResponseEntity.badRequest().body("Impossibile effettuare la prenotazione per questo evento. Posti esauriti.");
        }
    }
}


