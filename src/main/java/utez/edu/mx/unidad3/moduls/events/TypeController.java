package utez.edu.mx.unidad3.moduls.events;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.unidad3.moduls.events.dto.TypeRequestDto;
import utez.edu.mx.unidad3.utils.APIResponse;

@RestController
@RequestMapping("/api/types")
@Tag(name = "Tipos de Eventos", description = "API para gestionar tipos de eventos")
@CrossOrigin(origins = "*")
public class TypeController {

    @Autowired
    private TypeService typeService;

    @PostMapping
    @Operation(summary = "Crear un nuevo tipo de evento", description = "Crea un nuevo tipo de evento - solo administradores")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> createType(@Valid @RequestBody TypeRequestDto typeRequestDto) {
        APIResponse response = typeService.createType(typeRequestDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los tipos de eventos", description = "Obtiene una lista de todos los tipos de eventos disponibles")
    public ResponseEntity<APIResponse> getAllTypes() {
        APIResponse response = typeService.getAllTypes();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener tipo por ID", description = "Obtiene un tipo de evento específico por su ID")
    public ResponseEntity<APIResponse> getTypeById(@PathVariable Long id) {
        APIResponse response = typeService.getTypeById(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/name/{name}")
    @Operation(summary = "Obtener tipo por nombre", description = "Obtiene un tipo de evento específico por su nombre")
    public ResponseEntity<APIResponse> getTypeByName(@PathVariable String name) {
        APIResponse response = typeService.getTypeByName(name);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar tipo de evento", description = "Actualiza un tipo de evento existente - solo administradores")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> updateType(@PathVariable Long id, @Valid @RequestBody TypeRequestDto typeRequestDto) {
        APIResponse response = typeService.updateType(id, typeRequestDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // Actualizar tipo de evento por nombre
    @PutMapping("/name/{name}")
    @Operation(summary = "Actualizar tipo de evento por nombre", description = "Actualiza un tipo de evento existente por su nombre - solo administradores")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> updateTypeByName(@PathVariable String name, @Valid @RequestBody TypeRequestDto typeRequestDto) {
        APIResponse response = typeService.updateTypeByName(name, typeRequestDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar tipo de evento", description = "Elimina un tipo de evento - solo administradores")
    @SecurityRequirement(name = "bearerAuth")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<APIResponse> deleteType(@PathVariable Long id) {
        APIResponse response = typeService.deleteType(id);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/search")
    @Operation(summary = "Buscar tipos por descripción", description = "Busca tipos de eventos que contengan el texto en su descripción")
    public ResponseEntity<APIResponse> searchTypesByDescription(@RequestParam String description) {
        APIResponse response = typeService.searchTypesByDescription(description);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
