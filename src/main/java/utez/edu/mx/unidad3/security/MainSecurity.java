package utez.edu.mx.unidad3.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import utez.edu.mx.unidad3.security.filters.JWTFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
public class MainSecurity {
    @Autowired
    private JWTFilter jwtFilter;

    private final String[] SWAGGERS_URLS = {
            "/swagger-ui.html",
            "swagger-ui/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-resources/**",
            "/webjars/**"
    };

    @Bean
    public SecurityFilterChain doFilterInternal(HttpSecurity http) throws Exception {
        http.csrf(c -> c.disable()).cors(c -> c.configurationSource(corsRegistry()))
                .authorizeHttpRequests(auth -> auth
                        // Rutas públicas primero - más específicas
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/auth").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/auth/test-token/**").permitAll() // Solo test-token público
                        .requestMatchers(HttpMethod.POST, "/api/events").permitAll() // Permitir creación de eventos
                        .requestMatchers(HttpMethod.GET, "/api/events/**").permitAll() // Permitir consulta de eventos
                        .requestMatchers("/actuator/**").permitAll()
                        .requestMatchers(
                                SWAGGERS_URLS
                        ).permitAll()
                        // Rutas que requieren autenticación (incluyendo verify-token)
                        .requestMatchers(HttpMethod.GET, "/api/auth/verify-token").authenticated()
                        // Rutas protegidas después - Grupos requieren ADMIN o ADMINGROUP
                        .requestMatchers(HttpMethod.POST, "/api/group").hasAnyRole("ADMIN", "ADMINGROUP")
                        .requestMatchers(HttpMethod.PUT, "/api/group/**").hasAnyRole("ADMIN", "ADMINGROUP")
                        .requestMatchers(HttpMethod.DELETE, "/api/group/**").hasAnyRole("ADMIN", "ADMINGROUP")
                        .requestMatchers(HttpMethod.GET, "/api/group/**").hasAnyRole("ADMIN", "ADMINGROUP", "MEMBER")
                        // Operaciones de eventos que requieren permisos especiales
                        .requestMatchers(HttpMethod.PUT, "/api/events/update-status").hasAnyRole("ADMIN", "ADMINGROUP")
                        .requestMatchers(HttpMethod.PUT, "/api/events/*/status").hasAnyRole("ADMIN", "ADMINGROUP")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/delete").hasAnyRole("ADMIN", "ADMINGROUP")
                        .requestMatchers(HttpMethod.DELETE, "/api/events/*").hasAnyRole("ADMIN", "ADMINGROUP")
                        // Otras rutas protegidas
                        .requestMatchers("/api/cede/**").hasRole("ADMINGROUP")
                        .requestMatchers("/api/client/**").hasRole("ADMIN")
                        .requestMatchers("/api/events/**").authenticated() // Otras operaciones de eventos requieren autenticación
                        .anyRequest().authenticated()
                    ).sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    private CorsConfigurationSource corsRegistry() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("*"));
        configuration.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(false); // cookies

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
