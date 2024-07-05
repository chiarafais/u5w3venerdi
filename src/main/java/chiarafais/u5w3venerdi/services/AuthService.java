package chiarafais.u5w3venerdi.services;

import chiarafais.u5w3venerdi.entities.Utente;
import chiarafais.u5w3venerdi.exceptions.UnauthorizedException;
import chiarafais.u5w3venerdi.payloads.UtenteLoginDTO;
import chiarafais.u5w3venerdi.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {
    @Autowired
    private UtentiService utentiService;

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private PasswordEncoder bcrypt;

    public String authenticateUserAndGenerateToken(UtenteLoginDTO payload){

        Utente utente = this.utentiService.findByEmail(payload.email());
        // Verifico se la password combacia con quella ricevuta nel payload
        if (bcrypt.matches(payload.password(), utente.getPassword())) {
            // Genero un token e lo torno
            return jwtTools.createToken(utente);
        } else {
            throw new UnauthorizedException("Credenziali non valide! Impossibile accedere");
        }


    }
}
