package utez.edu.mx.unidad3.moduls.groups;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.unidad3.utils.APIResponse;

@RestController
@RequestMapping("/api/group")
@Tag(name = "controlador de grupos", description = "Operaciones relacionadas con los grupos")
@SecurityRequirement(name = "bearerAuth")
public class GroupController {
    @Autowired
    private GroupService groupService;

    @GetMapping("")
    @Operation(summary = "Traer todos los grupos", description = "Trea el estado de los grupos en el sistema")
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
            description = "Error al mostrar los grupos",
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
        APIResponse response = groupService.findAll();
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PostMapping("")
    @Operation(summary = "Registrar grupo", description = "Registra a un grupo en el sistema")
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
            description = "Error al registrar el grupo",
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
    public ResponseEntity<APIResponse> saveClient(@RequestBody @Valid Group payload) {
        APIResponse response = groupService.saveGroup(payload);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Consultar grupo por ID", description = "Consulta a un grupo por su ID en el sistema")
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
            description = "No se encontro al grupo",
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
        APIResponse response = groupService.findById(id);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @PutMapping("")
    @Operation(summary = "Actualizar grupo", description = "Actualiza a un grupo en el sistema")
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
            description = "Error al actualizar el grupo",
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
    public ResponseEntity<APIResponse> updateClient(@RequestBody Group payload) {
        APIResponse response = groupService.updateGroup(payload);
        return new ResponseEntity<>(response, response.getStatus());
    }

    @DeleteMapping("")
    @Operation(summary = "Eliminar grupo", description = "Elimina a un grupo en el sistema")
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
            description = "Error al eliminar el grupo",
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
    public ResponseEntity<APIResponse> deleteClient(@RequestBody Group payload) {
        APIResponse response = groupService.deleteGroup(payload);
        return new ResponseEntity<>(response, response.getStatus());
    }
}
