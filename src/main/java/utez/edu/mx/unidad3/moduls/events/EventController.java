package utez.edu.mx.unidad3.moduls.events;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.unidad3.moduls.events.dto.EventRequestDto;
import utez.edu.mx.unidad3.moduls.events.dto.EventStatusUpdateDto;
import utez.edu.mx.unidad3.utils.APIResponse;

@RestController
@RequestMapping("/api/events")
@CrossOrigin(origins = {"*"})
@Tag(name = "Eventos", description = "Gestión de eventos de grupos")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    @Operation(summary = "Crear un nuevo evento", description = "Crea un nuevo evento para un grupo específico")
    public ResponseEntity<APIResponse> createEvent(@Valid @RequestBody EventRequestDto eventRequestDto) {
        APIResponse response = eventService.createEvent(eventRequestDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping
    @Operation(summary = "Obtener todos los eventos", description = "Obtiene una lista de todos los eventos registrados")
    public ResponseEntity<APIResponse> getAllEvents() {
        APIResponse response = eventService.getAllEvents();
        return ResponseEntity.status(response.getStatus()).body(response);
    }


    @GetMapping("/upcoming")
    @Operation(summary = "Obtener eventos próximos", description = "Obtiene todos los eventos con fecha futura")
    public ResponseEntity<APIResponse> getUpcomingEvents() {
        APIResponse response = eventService.getUpcomingEvents();
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/status/{status}")
    @Operation(summary = "Obtener eventos por estado", description = "Obtiene eventos filtrados por su estado")
    public ResponseEntity<APIResponse> getEventsByStatus(@PathVariable EventStatus status) {
        APIResponse response = eventService.getEventsByStatus(status);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/find")
    @Operation(summary = "Obtener evento por título y grupo", description = "Obtiene un evento específico por título y nombre de grupo")
    public ResponseEntity<APIResponse> getEventByTitleAndGroup(
            @RequestParam String title,
            @RequestParam String groupName) {
        APIResponse response = eventService.getEventByTitleAndGroup(title, groupName);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-status")
    @Operation(summary = "Actualizar estado del evento por nombre", description = "Actualiza el estado de un evento usando título y nombre de grupo")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> updateEventStatusByName(
            @RequestParam String title,
            @RequestParam String groupName,
            @Valid @RequestBody EventStatusUpdateDto statusUpdateDto) {
        APIResponse response = eventService.updateEventStatusByName(title, groupName, statusUpdateDto);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar evento por nombre", description = "Elimina un evento usando título y nombre de grupo")
    public ResponseEntity<APIResponse> deleteEventByName(
            @RequestParam String title,
            @RequestParam String groupName) {
        APIResponse response = eventService.deleteEventByName(title, groupName);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/group/{groupName}/status/{status}")
    @Operation(summary = "Obtener eventos por grupo y estado", description = "Obtiene eventos filtrados por nombre de grupo y estado")
    public ResponseEntity<APIResponse> getEventsByGroupNameAndStatus(
            @PathVariable String groupName,
            @PathVariable EventStatus status) {
        APIResponse response = eventService.getEventsByGroupNameAndStatus(groupName, status);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
