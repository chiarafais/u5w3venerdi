package chiarafais.u5w3venerdi.controllers;

import chiarafais.u5w3venerdi.exceptions.BadRequestException;
import chiarafais.u5w3venerdi.payloads.NewUtenteDTO;
import chiarafais.u5w3venerdi.payloads.NewUtenteRespDTO;
import chiarafais.u5w3venerdi.payloads.UtenteLoginDTO;
import chiarafais.u5w3venerdi.payloads.UtenteLoginRespDTO;
import chiarafais.u5w3venerdi.services.AuthService;
import chiarafais.u5w3venerdi.services.UtentiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private UtentiService utentiService;

    //    POST http://localhost:3001/auth/login
    @PostMapping("/login")
    public UtenteLoginRespDTO login(@RequestBody UtenteLoginDTO payload){
        return new UtenteLoginRespDTO(this.authService.authenticateUserAndGenerateToken(payload));
    }

    //    POST http://localhost:3001/auth/register
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewUtenteRespDTO saveUser(@RequestBody @Validated NewUtenteDTO body, BindingResult validation){
        if(validation.hasErrors()) {
            throw new BadRequestException(validation.getAllErrors());
        }
        return new NewUtenteRespDTO(this.utentiService.saveUtente(body).getId());
    }

}