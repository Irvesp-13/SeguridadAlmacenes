package utez.edu.mx.unidad3.moduls.events;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import utez.edu.mx.unidad3.moduls.events.dto.EventRequestDto;
import utez.edu.mx.unidad3.moduls.events.dto.EventStatusUpdateDto;
import utez.edu.mx.unidad3.utils.APIResponse;

@RestController
@RequestMapping("/api/events")
@Tag(name = "Eventos", description = "API para gestionar eventos")
@CrossOrigin(origins = "*")
public class EventController {

    @Autowired
    private EventService eventService;

    @PostMapping
    @Operation(summary = "Crear un nuevo evento", description = "Crea un nuevo evento - requiere autenticación")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> createEvent(@Valid @RequestBody EventRequestDto eventRequestDto) {
        // Obtener el username del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String creatorUsername = authentication.getName();

        APIResponse response = eventService.createEvent(eventRequestDto, creatorUsername);
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

    @GetMapping("/creator/{creatorUsername}")
    @Operation(summary = "Obtener eventos por creador", description = "Obtiene todos los eventos creados por un usuario específico")
    public ResponseEntity<APIResponse> getEventsByCreator(@PathVariable String creatorUsername) {
        APIResponse response = eventService.getEventsByCreator(creatorUsername);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/find")
    @Operation(summary = "Obtener evento por título y creador", description = "Obtiene un evento específico por título y nombre de usuario creador")
    public ResponseEntity<APIResponse> getEventByTitleAndCreator(
            @RequestParam String title,
            @RequestParam String creatorUsername) {
        APIResponse response = eventService.getEventByTitleAndCreator(title, creatorUsername);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/type/{typeName}")
    @Operation(summary = "Obtener eventos por tipo", description = "Obtiene eventos filtrados por tipo")
    public ResponseEntity<APIResponse> getEventsByType(@PathVariable String typeName) {
        APIResponse response = eventService.getEventsByType(typeName);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/update-status")
    @Operation(summary = "Actualizar estado del evento", description = "Actualiza el estado de un evento usando título y creador - solo el creador o admin")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> updateEventStatus(
            @RequestParam String title,
            @RequestParam String creatorUsername,
            @Valid @RequestBody EventStatusUpdateDto statusUpdateDto) {

        // Obtener el username del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        APIResponse response = eventService.updateEventStatus(title, creatorUsername, statusUpdateDto, currentUsername);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/delete")
    @Operation(summary = "Eliminar evento", description = "Elimina un evento usando título y creador - solo el creador o admin")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> deleteEvent(
            @RequestParam String title,
            @RequestParam String creatorUsername) {

        // Obtener el username del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        APIResponse response = eventService.deleteEvent(title, creatorUsername, currentUsername);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/my-events")
    @Operation(summary = "Obtener mis eventos", description = "Obtiene todos los eventos creados por el usuario autenticado")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> getMyEvents() {
        // Obtener el username del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        APIResponse response = eventService.getEventsByCreator(username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar evento por ID", description = "Actualiza un evento existente por su ID - solo el creador o admin")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> updateEvent(
            @PathVariable Long id,
            @Valid @RequestBody EventRequestDto eventRequestDto) {

        // Obtener el username del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        APIResponse response = eventService.updateEvent(id, eventRequestDto, currentUsername);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    // ==================== ENDPOINTS PARA ASISTENCIA A EVENTOS ====================

    @PostMapping("/{eventId}/attend")
    @Operation(summary = "Registrar asistencia a un evento", description = "Permite al usuario autenticado registrarse para asistir a un evento")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> registerAttendance(@PathVariable Long eventId) {
        // Obtener el username del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        APIResponse response = eventService.registerAttendance(eventId, username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{eventId}/attend")
    @Operation(summary = "Cancelar asistencia a un evento", description = "Permite al usuario autenticado cancelar su asistencia a un evento")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> cancelAttendance(@PathVariable Long eventId) {
        // Obtener el username del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        APIResponse response = eventService.cancelAttendance(eventId, username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @DeleteMapping("/{eventId}/cancel-attendance")
    @Operation(summary = "Cancelar asistencia a un evento (endpoint alternativo)", description = "Endpoint alternativo para cancelar asistencia - mantiene compatibilidad con frontend")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> cancelAttendanceAlternative(@PathVariable Long eventId) {
        // Obtener el username del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        APIResponse response = eventService.cancelAttendance(eventId, username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/my-attendance")
    @Operation(summary = "Obtener eventos a los que voy a asistir", description = "Obtiene todos los eventos a los que el usuario autenticado va a asistir")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> getMyAttendanceEvents() {
        // Obtener el username del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        APIResponse response = eventService.getEventsByAttendee(username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{eventId}/attendees")
    @Operation(summary = "Obtener asistentes de un evento", description = "Obtiene la lista de usuarios que van a asistir a un evento específico")
    public ResponseEntity<APIResponse> getEventAttendees(@PathVariable Long eventId) {
        APIResponse response = eventService.getEventAttendees(eventId);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/{eventId}/check-attendance")
    @Operation(summary = "Verificar si estoy registrado para el evento", description = "Verifica si el usuario autenticado está registrado para asistir al evento")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> checkMyRegistration(@PathVariable Long eventId) {
        // Obtener el username del usuario autenticado
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String username = authentication.getName();

        APIResponse response = eventService.checkUserRegistration(eventId, username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }

    @GetMapping("/user/{username}/attendance")
    @Operation(summary = "Obtener eventos de asistencia de un usuario", description = "Obtiene eventos a los que asiste un usuario específico - solo para admins")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<APIResponse> getUserAttendanceEvents(@PathVariable String username) {
        APIResponse response = eventService.getEventsByAttendee(username);
        return ResponseEntity.status(response.getStatus()).body(response);
    }
}
