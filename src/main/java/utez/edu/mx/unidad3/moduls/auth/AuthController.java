package utez.edu.mx.unidad3.moduls.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.unidad3.moduls.auth.dto.LoginRequestDTO;
import utez.edu.mx.unidad3.moduls.user.User;
import utez.edu.mx.unidad3.security.jwt.JWTUtils;
import utez.edu.mx.unidad3.security.jwt.UDService;
import utez.edu.mx.unidad3.utils.APIResponse;

import java.util.Collection;
import java.util.stream.Collectors;

@RequestMapping("/api/auth")
@RestController
@Tag(name = "controlador de sesiones", description = "Operaciones relacionadas con las sesiones y registros")
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UDService uDService;

    @PostMapping("")
    @Operation(summary = "Inicio de sesión", description = "Inicia sesión en el sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Respuesta de operacion exitosa",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))
                    }
            )
            , @ApiResponse(
            responseCode = "400",
            description = "Error al iniciar sesión",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))
            }
    )
            , @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))
            }
    )
    })
    public ResponseEntity<APIResponse> doLogin(@RequestBody LoginRequestDTO payload) {
        APIResponse response = authService.doLogin(payload);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("/register")
    @Operation(summary = "Registrar usuario", description = "Registra un usuario en el sistema")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Respuesta de operacion exitosa",
                    content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))
                    }
            )
            , @ApiResponse(
            responseCode = "400",
            description = "Error al registrar usuario",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))
            }
    )
            , @ApiResponse(
            responseCode = "500",
            description = "Error interno del servidor",
            content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = APIResponse.class))
            }
    )
    })
    public ResponseEntity<APIResponse> doRegister(@RequestBody User payload) {
        APIResponse response = authService.register(payload);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/verify-token")
    @Operation(summary = "Verificar token", description = "Verifica si el token JWT es válido y muestra información del usuario")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> verifyToken(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new APIResponse("Token inválido o no autenticado", true, HttpStatus.UNAUTHORIZED));
            }

            String username = authentication.getName();
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();

            String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(", "));

            String message = String.format("Token válido. Usuario: %s, Roles: %s", username, roles);

            return ResponseEntity.ok(new APIResponse(message, username, false, HttpStatus.OK));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIResponse("Error al verificar token: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }

    @GetMapping("/test-token/{token}")
    @Operation(summary = "Probar token como parámetro", description = "Prueba validar un token JWT pasado como parámetro de URL")
    public ResponseEntity<APIResponse> testToken(@PathVariable String token) {
        try {
            // Inyectar dependencias necesarias
            if (jwtUtils == null) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new APIResponse("JWTUtils no está disponible", true, HttpStatus.INTERNAL_SERVER_ERROR));
            }

            // Extraer username del token
            String username = jwtUtils.extractUsername(token);
            if (username == null) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new APIResponse("No se pudo extraer el username del token", true, HttpStatus.BAD_REQUEST));
            }

            // Cargar usuario
            UserDetails userDetails = uDService.loadUserByUsername(username);

            // Validar token
            boolean isValid = jwtUtils.validateToken(token, userDetails);

            if (isValid) {
                String roles = userDetails.getAuthorities().stream()
                    .map(GrantedAuthority::getAuthority)
                    .collect(Collectors.joining(", "));

                String message = String.format("Token VÁLIDO. Usuario: %s, Roles: %s", username, roles);
                return ResponseEntity.ok(new APIResponse(message, username, false, HttpStatus.OK));
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new APIResponse("Token INVÁLIDO - falló la validación", true, HttpStatus.UNAUTHORIZED));
            }

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new APIResponse("Error al procesar token: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR));
        }
    }
}
