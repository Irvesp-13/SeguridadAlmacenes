package utez.edu.mx.unidad3.security.filters;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import utez.edu.mx.unidad3.security.jwt.JWTUtils;
import utez.edu.mx.unidad3.security.jwt.UDService;

import java.io.IOException;

@Component
public class JWTFilter extends OncePerRequestFilter {
    @Autowired
    private UDService uDService;

    @Autowired
    private JWTUtils jwtUtils;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String AUTHORIZATION_HEADER = request.getHeader("Authorization");
        String username = null;
        String token = null;

        System.out.println("=== JWT FILTER DEBUG ===");
        System.out.println("Request URI: " + request.getRequestURI());
        System.out.println("Authorization Header: " + AUTHORIZATION_HEADER);

        try {
            if (AUTHORIZATION_HEADER != null && AUTHORIZATION_HEADER.startsWith("Bearer ")) {
                token = AUTHORIZATION_HEADER.substring(7);
                System.out.println("Token extraído: " + token.substring(0, Math.min(20, token.length())) + "...");

                username = jwtUtils.extractUsername(token);
                System.out.println("Username extraído: " + username);
            } else {
                System.out.println("No hay header Authorization o no empieza con 'Bearer '");
            }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("Procesando autenticación para usuario: " + username);

                UserDetails userDetails = uDService.loadUserByUsername(username);
                System.out.println("UserDetails cargado: " + userDetails.getUsername());
                System.out.println("Authorities: " + userDetails.getAuthorities());

                if (jwtUtils.validateToken(token, userDetails)) {
                    System.out.println("Token VÁLIDO - Estableciendo autenticación");

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities()
                    );
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    // Guardar el token en el contexto de seguridad
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("Autenticación establecida exitosamente");
                } else {
                    System.out.println("Token INVÁLIDO - No se estableció autenticación");
                }
            } else if (username != null) {
                System.out.println("Usuario ya autenticado en el contexto");
            } else {
                System.out.println("No se extrajo username del token");
            }
        } catch (Exception e) {
            System.err.println("Error en JWTFilter: " + e.getMessage());
            e.printStackTrace();
        }

        System.out.println("=== FIN JWT FILTER DEBUG ===");
        filterChain.doFilter(request, response);
    }
}
