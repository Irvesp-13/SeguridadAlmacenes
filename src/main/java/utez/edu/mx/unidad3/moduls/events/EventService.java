package utez.edu.mx.unidad3.moduls.events;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.unidad3.moduls.events.dto.EventRequestDto;
import utez.edu.mx.unidad3.moduls.events.dto.EventResponseDto;
import utez.edu.mx.unidad3.moduls.events.dto.EventStatusUpdateDto;
import utez.edu.mx.unidad3.moduls.groups.Group;
import utez.edu.mx.unidad3.moduls.groups.GroupRepository;
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
    private GroupRepository groupRepository;

    // Crear un nuevo evento
    public APIResponse createEvent(EventRequestDto eventRequestDto) {
        try {
            // Verificar que el grupo existe por nombre
            Optional<Group> groupOpt = groupRepository.findByName(eventRequestDto.getGroupName());
            if (groupOpt.isEmpty()) {
                return new APIResponse("Grupo no encontrado con el nombre: " + eventRequestDto.getGroupName(), true, HttpStatus.NOT_FOUND);
            }

            // Verificar que la fecha del evento sea futura
            if (eventRequestDto.getEventDate().isBefore(LocalDateTime.now())) {
                return new APIResponse("La fecha del evento debe ser futura", true, HttpStatus.BAD_REQUEST);
            }

            Group group = groupOpt.get();
            Event event = new Event(
                eventRequestDto.getTitle(),
                eventRequestDto.getEventDate(),
                eventRequestDto.getEventType(),
                group
            );

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

    // Obtener eventos por grupo
    public APIResponse getEventsByGroup(Long groupId) {
        try {
            // Verificar que el grupo existe
            if (!groupRepository.existsById(groupId)) {
                return new APIResponse("Grupo no encontrado", true, HttpStatus.NOT_FOUND);
            }

            List<Event> events = eventRepository.findByGroupIdOrderByEventDateDesc(groupId);
            List<EventResponseDto> responseList = events.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

            return new APIResponse("Eventos del grupo obtenidos exitosamente", responseList, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener eventos por grupo usando nombre
    public APIResponse getEventsByGroupName(String groupName) {
        try {
            // Verificar que el grupo existe
            Optional<Group> groupOpt = groupRepository.findByName(groupName);
            if (groupOpt.isEmpty()) {
                return new APIResponse("Grupo no encontrado con el nombre: " + groupName, true, HttpStatus.NOT_FOUND);
            }

            List<Event> events = eventRepository.findByGroupName(groupName);
            List<EventResponseDto> responseList = events.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

            return new APIResponse("Eventos del grupo obtenidos exitosamente", responseList, HttpStatus.OK);
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

    // Obtener evento por título y nombre de grupo
    public APIResponse getEventByTitleAndGroup(String title, String groupName) {
        try {
            Optional<Event> eventOpt = eventRepository.findByTitleAndGroupName(title, groupName);
            if (eventOpt.isEmpty()) {
                return new APIResponse("Evento no encontrado con título '" + title + "' en el grupo '" + groupName + "'", true, HttpStatus.NOT_FOUND);
            }

            EventResponseDto responseDto = convertToResponseDto(eventOpt.get());
            return new APIResponse("Evento obtenido exitosamente", responseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar estado del evento (solo para administradores)
    public APIResponse updateEventStatus(Long eventId, EventStatusUpdateDto statusUpdateDto) {
        try {
            Optional<Event> eventOpt = eventRepository.findById(eventId);
            if (eventOpt.isEmpty()) {
                return new APIResponse("Evento no encontrado", true, HttpStatus.NOT_FOUND);
            }

            Event event = eventOpt.get();
            event.setStatus(statusUpdateDto.getStatus());
            Event updatedEvent = eventRepository.save(event);

            EventResponseDto responseDto = convertToResponseDto(updatedEvent);
            return new APIResponse("Estado del evento actualizado exitosamente", responseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Actualizar estado del evento usando título y nombre de grupo
    public APIResponse updateEventStatusByName(String title, String groupName, EventStatusUpdateDto statusUpdateDto) {
        try {
            Optional<Event> eventOpt = eventRepository.findByTitleAndGroupName(title, groupName);
            if (eventOpt.isEmpty()) {
                return new APIResponse("Evento no encontrado con título '" + title + "' en el grupo '" + groupName + "'", true, HttpStatus.NOT_FOUND);
            }

            Event event = eventOpt.get();
            event.setStatus(statusUpdateDto.getStatus());
            Event updatedEvent = eventRepository.save(event);

            EventResponseDto responseDto = convertToResponseDto(updatedEvent);
            return new APIResponse("Estado del evento actualizado exitosamente", responseDto, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener eventos próximos
    public APIResponse getUpcomingEvents() {
        try {
            List<Event> events = eventRepository.findUpcomingEvents(LocalDateTime.now());
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

    // Eliminar evento
    public APIResponse deleteEvent(Long eventId) {
        try {
            if (!eventRepository.existsById(eventId)) {
                return new APIResponse("Evento no encontrado", true, HttpStatus.NOT_FOUND);
            }

            eventRepository.deleteById(eventId);
            return new APIResponse("Evento eliminado exitosamente", true, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar evento usando título y nombre de grupo
    public APIResponse deleteEventByName(String title, String groupName) {
        try {
            Optional<Event> eventOpt = eventRepository.findByTitleAndGroupName(title, groupName);
            if (eventOpt.isEmpty()) {
                return new APIResponse("Evento no encontrado con título '" + title + "' en el grupo '" + groupName + "'", true, HttpStatus.NOT_FOUND);
            }

            eventRepository.delete(eventOpt.get());
            return new APIResponse("Evento eliminado exitosamente", true, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Obtener eventos por estado y nombre de grupo
    public APIResponse getEventsByGroupNameAndStatus(String groupName, EventStatus status) {
        try {
            // Verificar que el grupo existe
            Optional<Group> groupOpt = groupRepository.findByName(groupName);
            if (groupOpt.isEmpty()) {
                return new APIResponse("Grupo no encontrado con el nombre: " + groupName, true, HttpStatus.NOT_FOUND);
            }

            List<Event> events = eventRepository.findByGroupNameAndStatus(groupName, status);
            List<EventResponseDto> responseList = events.stream()
                .map(this::convertToResponseDto)
                .collect(Collectors.toList());

            return new APIResponse("Eventos filtrados obtenidos exitosamente", responseList, HttpStatus.OK);
        } catch (Exception e) {
            return new APIResponse("Error interno del servidor: " + e.getMessage(), true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Método auxiliar para convertir Event a EventResponseDto
    private EventResponseDto convertToResponseDto(Event event) {
        return new EventResponseDto(
            event.getId(),
            event.getTitle(),
            event.getEventDate(),
            event.getEventType(),
            event.getStatus(),
            event.getGroup().getId(),
            event.getGroup().getName(),
            event.getCreatedAt(),
            event.getUpdatedAt()
        );
    }
}
