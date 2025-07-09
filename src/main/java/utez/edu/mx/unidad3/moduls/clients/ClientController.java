package utez.edu.mx.unidad3.moduls.clients;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.unidad3.utils.APIResponse;

@RestController
@RequestMapping("/api/client")
@Tag(name = "controlador de clientes", description = "Operaciones relacionadas con clientes")
@SecurityRequirement(name = "bearerAuth")
public class ClientController {
    @Autowired
    private ClientService clientService;

    @GetMapping("")
    @Operation(summary = "Traer todos los clientes", description = "Trea el estado de los clientes en el sistema")
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
            description = "Error al mostrar los clientes",
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
    public ResponseEntity<APIResponse> findAll(){
        APIResponse response = clientService.findAll();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("")
    @Operation(summary = "Registrar cliente", description = "Registra a un cliente en el sistema")
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
                    description = "Error al registrar el cliente",
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
    public ResponseEntity<APIResponse> saveClient(@RequestBody Client payload) {
        APIResponse response = clientService.saveClient(payload);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar cliente por ID", description = "Consulta a un cliente por su ID en el sistema")
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
            description = "No se encontro al cliente",
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
    public ResponseEntity<APIResponse> findById(@PathVariable("id") Long id) {
        APIResponse response = clientService.findById(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("")
    @Operation(summary = "Actualizar cliente", description = "Actualiza a un cliente en el sistema")
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
            description = "Error al actualizar el cliente",
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
    public ResponseEntity<APIResponse> updateClient(@RequestBody Client payload) {
        APIResponse response = clientService.updateClient(payload);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("")
    @Operation(summary = "Eliminar cliente", description = "Elimina a un cliente en el sistema")
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
            description = "Error al eliminar el cliente",
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
    public ResponseEntity<APIResponse> deleteClient(@RequestBody Client payload) {
        APIResponse response = clientService.deleteClient(payload);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
