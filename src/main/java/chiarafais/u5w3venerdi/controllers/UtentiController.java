package chiarafais.u5w3venerdi.controllers;

import chiarafais.u5w3venerdi.entities.Evento;
import chiarafais.u5w3venerdi.entities.Utente;
import chiarafais.u5w3venerdi.exceptions.BadRequestException;
import chiarafais.u5w3venerdi.payloads.NewUtenteDTO;
import chiarafais.u5w3venerdi.payloads.NewUtenteRespDTO;
import chiarafais.u5w3venerdi.services.UtentiService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/utenti")
public class UtentiController {
    @Autowired
    private UtentiService utentiService;

    //    1. POST http://localhost:3001/utenti (+ body)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewUtenteRespDTO saveUtente(@RequestBody @Validated NewUtenteDTO body, BindingResult validation){

        if(validation.hasErrors()) {
            System.out.println(validation.getAllErrors());
            throw new BadRequestException(validation.getAllErrors());
        }
        System.out.println(body);
        return new NewUtenteRespDTO(this.utentiService.saveUtente(body).getId());}

    @GetMapping("/me")
    public Utente getProfile(@AuthenticationPrincipal Utente currentAuthenticatedUtente){
        return currentAuthenticatedUtente;
    }

    @PutMapping("/me")
    public Utente updateProfile(@AuthenticationPrincipal Utente currentAuthenticatedUtente, @RequestBody Utente updatedUtente){
        return this.utentiService.findByIdAndUpdate(currentAuthenticatedUtente.getId(), updatedUtente);
    }

    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Utente currentAuthenticatedUtente){
        this.utentiService.findByIdAndDelete(currentAuthenticatedUtente.getId());
    }



    // 2. GET http://localhost:3001/utenti/{{utenteId}}
    @GetMapping("/{utenteId}")
    private Utente findUtenteById(@PathVariable int utenteId){
        return this.utentiService.findById(utenteId);
    }

    //    3. GET http://localhost:3001/utenti (+authorization bear token da organizzatore)
    @GetMapping
    @PreAuthorize("hasAuthority('ORGANIZZATORE')")
    public List<Utente> getAllUtenti(){
        return this.utentiService.getUtentiList();
    }

    //    3.1 Paginazione e ordinamento http://localhost:3001/utenti/page
    @GetMapping("/page")
    public Page<Utente> getAllUtenti(@RequestParam(defaultValue = "0") int page,
                                     @RequestParam(defaultValue = "2") int size,
                                     @RequestParam(defaultValue = "id") String sortBy) {
        return this.utentiService.getUtenti(page, size, sortBy);
    }


    // 4. PUT http://localhost:3001/utenti/{{utenteId}} (+ body)
    @PutMapping("/{utenteId}")
    private Utente findByIdAndUpdate(@PathVariable int utenteId, @RequestBody Utente body){
        return this.utentiService.findByIdAndUpdate(utenteId, body);
    }



    // 5. DELETE http://localhost:3001/utenti/{utenteId}
    @DeleteMapping("/{utenteId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteUtenteById(@PathVariable int utenteId) {
        this.utentiService.findByIdAndDelete(utenteId);
    }

    //     GET http://localhost:3001/utenti/{utenteId}/prenotazioni (+authorization bear token da partecipante)
    @GetMapping("/{utenteId}/prenotazioni")
    @PreAuthorize("hasAuthority('PARTECIPANTE')") //solo un utente di tipo partecipante può visualizzare le sue prenotazioni
    public ResponseEntity<List<Evento>> getPrenotazioniUtente(@PathVariable int utenteId) {
        List<Evento> prenotazioni = utentiService.getPrenotazioniUtente(utenteId);
        return ResponseEntity.ok(prenotazioni);
    }

    //     DELETE http://localhost:3001/utenti/{utenteId}/prenotazioni/{eventoId} (+authorization bear token da partecipante)
    @DeleteMapping("/{utenteId}/prenotazioni/{eventoId}")
    @PreAuthorize("hasAuthority('PARTECIPANTE')") //solo un utente di tipo partecipante può eliminare una sua prenotazione
    public ResponseEntity<String> annullaPrenotazione(@PathVariable int utenteId, @PathVariable int eventoId) {
        boolean annullamentoRiuscito = utentiService.annullaPrenotazione(utenteId, eventoId);
        if (annullamentoRiuscito) {
            return ResponseEntity.ok("Prenotazione annullata con successo!");
        } else {
            return ResponseEntity.badRequest().body("Impossibile annullare la prenotazione per questo evento.");
        }
    }

}
