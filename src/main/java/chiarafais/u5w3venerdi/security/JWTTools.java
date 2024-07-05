package chiarafais.u5w3venerdi.security;

import chiarafais.u5w3venerdi.entities.Utente;
import chiarafais.u5w3venerdi.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(Utente utente){
        return Jwts.builder()
                .issuedAt(
                        new Date(
                                System
                                        .currentTimeMillis())) // Data di emissione del token (IAT - Issued AT) in
                // millisecondi
                .expiration(
                        new Date(
                                System.currentTimeMillis()
                                        + 1000 * 60 * 60 * 24
                                        * 7)) // Data di scadenza del token (Expiration Date) in millisecondi
                .subject(
                        String.valueOf(
                                utente.getId()))
                .signWith(
                        Keys.hmacShaKeyFor(secret.getBytes())) // Firmo il token con algoritmo HMAC passandogli il SEGRETO
                .compact();
    }

    public void verifyToken(String token){
        try {
            Jwts.parser()
                    .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                    .build().parse(token);
            // Il metodo .parse(token) mi lancerà delle eccezioni in caso di token scaduto o token manipolato
        } catch (Exception ex) {
            throw new UnauthorizedException("Token non valido!");

        }

    }

    public String extractIdFromToken(String token){
        return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(secret.getBytes()))
                .build().parseSignedClaims(token).getPayload().getSubject(); // Il subject è l'id dell'utente
    }
}