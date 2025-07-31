package utez.edu.mx.unidad3.moduls.auth;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import utez.edu.mx.unidad3.moduls.auth.dto.LoginRequestDTO;
import utez.edu.mx.unidad3.moduls.user.User;
import utez.edu.mx.unidad3.utils.APIResponse;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/auth")
@RestController
@Tag(name = "controlador de sesiones", description = "Operaciones relacionadas con las sesiones y registros")
public class AuthController {
    @Autowired
    private AuthService authService;

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

    @GetMapping("")
    @Operation(summary = "Usuarios existentes", description = "Se muestran los usuarios existentes en el sistema")
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
            description = "Error al mostrar los usuarios",
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
    public ResponseEntity<APIResponse> findAll() {
        APIResponse response = authService.findAll();
        return new ResponseEntity<>(response, response.getStatus());
    }
}
