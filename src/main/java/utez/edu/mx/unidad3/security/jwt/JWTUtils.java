package utez.edu.mx.unidad3.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JWTUtils {
    @Value("${secret.key}")
    private String SECRET_KEY;

    public String extractUsername(String token) {
        return extractClaims(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaims(token, Claims::getExpiration);
    }

    public <T> T extractClaims(String token, Function<Claims, T> claimsResolver) {
        final Claims CLAIMS = extractAllClaims(token);
        return claimsResolver.apply(CLAIMS);
    }

    public Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    // Validar que el token no este expirado
    public Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Consume la funcion de arriba adicional a que pregunta que el usuario del token concida
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String USERNAME = extractUsername(token);
        return (USERNAME.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    // Crea el token a raiz de la informacion del usuario
    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder() // Vamos a construir el token
                .setClaims(claims).setSubject(subject) // Informacion del usuario
                .setIssuedAt(new Date(System.currentTimeMillis())) // Cuando se creo el token
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // Hasta cuando sera valido
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // Sellamos el token
                .compact(); // Construir el token
    }

    // Consume la funcion de crear para solamente exportar el token
    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();

        // Agregar los roles/authorities al token
        Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
        if (authorities != null && !authorities.isEmpty()) {
            // Extraer los nombres de los roles
            String roles = authorities.stream()
                    .map(GrantedAuthority::getAuthority)
                    .reduce((a, b) -> a + "," + b)
                    .orElse("");
            claims.put("roles", roles);
        }

        return createToken(claims, userDetails.getUsername());
    }

    // Método para extraer roles del token
    public String extractRoles(String token) {
        return extractClaims(token, claims -> claims.get("roles", String.class));
    }
}
