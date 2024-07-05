package chiarafais.u5w3venerdi.security;

import chiarafais.u5w3venerdi.entities.Utente;
import chiarafais.u5w3venerdi.exceptions.UnauthorizedException;
import chiarafais.u5w3venerdi.services.UtentiService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private UtentiService utentiService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        //  Mancanza di Authorization Header -> 401
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer ")) throw new UnauthorizedException("Inserisci il token nell'Authorization Header");

        //  Se c'è estraiamo il token dall'header
        String accessToken = authHeader.substring(7);

        //  Verifica della signature e verifica Expiration Date
        jwtTools.verifyToken(accessToken);


        //  Cerco l'utente nel DB tramite id (l'id sta nel token..)
        String id = jwtTools.extractIdFromToken(accessToken);
        int dipendenteId = Integer.parseInt(id);
        Utente currentUtente = this.utentiService.findById(dipendenteId);

        //  Associo l'utente alla richiesta corrente
        Authentication authentication = new UsernamePasswordAuthenticationToken(currentUtente, null, currentUtente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        filterChain.doFilter(request, response);
    }


    @Override
    protected boolean shouldNotFilter(HttpServletRequest request){

        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
