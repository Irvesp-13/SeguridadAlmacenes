package utez.edu.mx.unidad3.moduls.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.unidad3.moduls.events.dto.EventRequestDto;
import utez.edu.mx.unidad3.moduls.events.dto.EventResponseDto;
import utez.edu.mx.unidad3.moduls.events.dto.EventStatusUpdateDto;
import utez.edu.mx.unidad3.moduls.user.User;
import utez.edu.mx.unidad3.moduls.user.UserRepository;
import utez.edu.mx.unidad3.utils.APIResponse;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TypeRepository typeRepository;

    // Crear un nuevo evento
    public APIResponse createEvent(EventRequestDto eventRequestDto, String creatorUsername) {
        try {
            // Verificar que el usuario creador existe
            Optional<User> creatorOpt = userRepository.findByUsername(creatorUsername);
            if (creatorOpt.isEmpty()) {
                return new APIResponse("Usuario creador no encontrado: " + creatorUsername, true, HttpStatus.NOT_FOUND);
            }

            // Verificar que el tipo existe por nombre
            Optional<Type> typeOpt = typeRepository.findByName(eventRequestDto.getEventType());
            if (typeOpt.isEmpty()) {
                return new APIResponse("Tipo de evento no encontrado con el nombre: " + eventRequestDto.getEventType(), true, HttpStatus.NOT_FOUND);
            }

            // Verificar que la fecha del evento sea futura
            if (eventRequestDto.getEventDate().isBefore(LocalDateTime.now())) {
                return new APIResponse("La fecha del evento debe ser futura", true, HttpStatus.BAD_REQUEST);
            }

            User creator = creatorOpt.get();
            Type type = typeOpt.get();

            Event event = new Event(
                eventRequestDto.getTitle(),
                eventRequestDto.getEventDate(),
                creator,
                type
            );

            // Agregar descripción y ubicación si están presentes
            if (eventRequestDto.getDescription() != null) {
                event.setDescription(eventRequestDto.getDescription());
            }
            if (eventRequestDto.getLocation() != null) {
                event.setLocation(eventRequestDto.getLocation());
            }

            Event savedEvent = eventRepository.save(event);
            EventResponseDto responseDto = convertToResponseDto(savedEvent);

            return new APIResponse("Evento creado exitosamente", responseDto, HttpStatus.CREATED);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener todos los eventos
    public APIResponse getAllEvents() {
        try {
            List<Event> events = eventRepository.findAll();
            List<EventResponseDto> responseList = events.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

            return new APIResponse("Eventos obtenidos exitosamente", responseList, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener eventos por creador
    public APIResponse getEventsByCreator(String creatorUsername) {
        try {
            // Verificar que el usuario existe
            Optional<User> creatorOpt = userRepository.findByUsername(creatorUsername);
            if (creatorOpt.isEmpty()) {
                return new APIResponse("Usuario no encontrado: " + creatorUsername, true, HttpStatus.NOT_FOUND);
            }

            List<Event> events = eventRepository.findByCreatorUsername(creatorUsername);
            List<EventResponseDto> responseList = events.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

            return new APIResponse("Eventos del usuario obtenidos exitosamente", responseList, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener un evento por ID
    public APIResponse getEventById(Long id) {
        try {
            Optional<Event> eventOpt = eventRepository.findById(id);
            if (eventOpt.isEmpty()) {
                return new APIResponse("Evento no encontrado", true, HttpStatus.NOT_FOUND);
            }

            EventResponseDto responseDto = convertToResponseDto(eventOpt.get());
            return new APIResponse("Evento obtenido exitosamente", responseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener evento por título y creador
    public APIResponse getEventByTitleAndCreator(String title, String creatorUsername) {
        try {
            Optional<Event> eventOpt = eventRepository.findByTitleAndCreatorUsername(title, creatorUsername);
            if (eventOpt.isEmpty()) {
                return new APIResponse("Evento no encontrado con título '" + title + "' del usuario '" + creatorUsername + "'", true, HttpStatus.NOT_FOUND);
            }

            EventResponseDto responseDto = convertToResponseDto(eventOpt.get());
            return new APIResponse("Evento obtenido exitosamente", responseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar estado del evento (solo el creador o admin)
    public APIResponse updateEventStatus(String title, String creatorUsername, EventStatusUpdateDto statusUpdateDto, String currentUsername) {
        try {
            Optional<Event> eventOpt = eventRepository.findByTitleAndCreatorUsername(title, creatorUsername);
            if (eventOpt.isEmpty()) {
                return new APIResponse("Evento no encontrado con título '" + title + "' del usuario '" + creatorUsername + "'", true, HttpStatus.NOT_FOUND);
            }

            Event event = eventOpt.get();

            // Verificar que el usuario actual es el creador del evento o es admin
            if (!event.getCreator().getUsername().equals(currentUsername)) {
                Optional<User> currentUserOpt = userRepository.findByUsername(currentUsername);
                if (currentUserOpt.isEmpty() || !isAdminUser(currentUserOpt.get())) {
                    return new APIResponse("No tienes permisos para actualizar este evento", true, HttpStatus.FORBIDDEN);
                }
            }

            event.setStatus(statusUpdateDto.getStatus());
            Event updatedEvent = eventRepository.save(event);

            EventResponseDto responseDto = convertToResponseDto(updatedEvent);
            return new APIResponse("Estado del evento actualizado exitosamente", responseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener eventos próximos públicos
    public APIResponse getUpcomingEvents() {
        try {
            List<Event> events = eventRepository.findPublicUpcomingEvents(LocalDateTime.now());
            List<EventResponseDto> responseList = events.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

            return new APIResponse("Eventos próximos obtenidos exitosamente", responseList, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener eventos por estado
    public APIResponse getEventsByStatus(EventStatus status) {
        try {
            List<Event> events = eventRepository.findByStatus(status);
            List<EventResponseDto> responseList = events.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

            return new APIResponse("Eventos filtrados por estado obtenidos exitosamente", responseList, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar evento (solo el creador o admin)
    public APIResponse deleteEvent(String title, String creatorUsername, String currentUsername) {
        try {
            Optional<Event> eventOpt = eventRepository.findByTitleAndCreatorUsername(title, creatorUsername);
            if (eventOpt.isEmpty()) {
                return new APIResponse("Evento no encontrado con título '" + title + "' del usuario '" + creatorUsername + "'", true, HttpStatus.NOT_FOUND);
            }

            Event event = eventOpt.get();

            // Verificar que el usuario actual es el creador del evento o es admin
            if (!event.getCreator().getUsername().equals(currentUsername)) {
                Optional<User> currentUserOpt = userRepository.findByUsername(currentUsername);
                if (currentUserOpt.isEmpty() || !isAdminUser(currentUserOpt.get())) {
                    return new APIResponse("No tienes permisos para eliminar este evento", true, HttpStatus.FORBIDDEN);
                }
            }

            eventRepository.delete(event);
            return new APIResponse("Evento eliminado exitosamente", true, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Buscar eventos por tipo
    public APIResponse getEventsByType(String typeName) {
        try {
            List<Event> events = eventRepository.findByTypeName(typeName);
            List<EventResponseDto> responseList = events.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

            return new APIResponse("Eventos filtrados por tipo obtenidos exitosamente", responseList, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar evento por ID
    public APIResponse updateEvent(Long id, EventRequestDto eventRequestDto, String currentUsername) {
        try {
            // Buscar el evento por ID
            Optional<Event> eventOptional = eventRepository.findById(id);
            if (eventOptional.isEmpty()) {
                return new APIResponse("Evento no encontrado", true, HttpStatus.NOT_FOUND);
            }

            Event existingEvent = eventOptional.get();

            // Verificar que el usuario actual sea el creador del evento o sea admin
            if (!existingEvent.getCreator().getUsername().equals(currentUsername)) {
                Optional<User> currentUserOpt = userRepository.findByUsername(currentUsername);
                if (currentUserOpt.isEmpty() || !isAdminUser(currentUserOpt.get())) {
                    return new APIResponse("No tienes permisos para actualizar este evento", true, HttpStatus.FORBIDDEN);
                }
            }

            // Buscar el tipo por nombre
            Optional<Type> typeOptional = typeRepository.findByName(eventRequestDto.getEventType());
            if (typeOptional.isEmpty()) {
                return new APIResponse("Tipo de evento no encontrado: " + eventRequestDto.getEventType(), true, HttpStatus.BAD_REQUEST);
            }

            // Verificar que la fecha del evento sea futura
            if (eventRequestDto.getEventDate().isBefore(LocalDateTime.now())) {
                return new APIResponse("La fecha del evento debe ser futura", true, HttpStatus.BAD_REQUEST);
            }

            // Actualizar los campos del evento
            existingEvent.setTitle(eventRequestDto.getTitle());
            existingEvent.setEventDate(eventRequestDto.getEventDate());
            existingEvent.setType(typeOptional.get());
            existingEvent.setDescription(eventRequestDto.getDescription());
            existingEvent.setLocation(eventRequestDto.getLocation());

            // Guardar el evento actualizado
            Event updatedEvent = eventRepository.save(existingEvent);
            EventResponseDto responseDto = convertToResponseDto(updatedEvent);

            return new APIResponse("Evento actualizado exitosamente", responseDto, HttpStatus.OK);

        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Método auxiliar para verificar si un usuario es admin
    private boolean isAdminUser(User user) {
        return "ADMIN".equals(user.getRol().getName()) || "ADMINGROUP".equals(user.getRol().getName());
    }

    // Método auxiliar para convertir Event a EventResponseDto
    private EventResponseDto convertToResponseDto(Event event) {
        return new EventResponseDto(
            event.getId(),
            event.getTitle(),
            event.getEventDate(),
            event.getType().getName(),
            event.getStatus(),
            event.getCreator().getId(),
            event.getCreator().getUsername(),
            event.getCreator().getNombreCompleto(),
            event.getDescription(),
            event.getLocation(),
            event.getCreatedAt(),
            event.getUpdatedAt()
        );
    }
}
