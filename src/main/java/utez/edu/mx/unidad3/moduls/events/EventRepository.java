package utez.edu.mx.unidad3.moduls.events;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    // Buscar eventos por ID del creador
    List<Event> findByCreatorId(Long creatorId);

    // Buscar eventos por nombre de usuario del creador
    @Query("SELECT e FROM Event e WHERE e.creator.username = :username ORDER BY e.eventDate DESC")
    List<Event> findByCreatorUsername(@Param("username") String username);

    // Buscar evento por título y creador
    @Query("SELECT e FROM Event e WHERE e.title = :title AND e.creator.username = :username")
    Optional<Event> findByTitleAndCreatorUsername(@Param("title") String title, @Param("username") String username);

    // Buscar eventos por estado
    List<Event> findByStatus(EventStatus status);

    // Buscar eventos por creador y estado
    @Query("SELECT e FROM Event e WHERE e.creator.username = :username AND e.status = :status")
    List<Event> findByCreatorUsernameAndStatus(@Param("username") String username, @Param("status") EventStatus status);

    // Buscar eventos próximos (fecha futura)
    @Query("SELECT e FROM Event e WHERE e.eventDate > :currentDate ORDER BY e.eventDate ASC")
    List<Event> findUpcomingEvents(@Param("currentDate") LocalDateTime currentDate);

    // Buscar eventos de un creador ordenados por fecha
    @Query("SELECT e FROM Event e WHERE e.creator.id = :creatorId ORDER BY e.eventDate DESC")
    List<Event> findByCreatorIdOrderByEventDateDesc(@Param("creatorId") Long creatorId);

    // Buscar eventos por tipo (usando la relación)
    @Query("SELECT e FROM Event e WHERE LOWER(e.type.name) LIKE LOWER(CONCAT('%', :typeName, '%'))")
    List<Event> findByTypeNameContainingIgnoreCase(@Param("typeName") String typeName);

    // Buscar eventos por tipo exacto
    @Query("SELECT e FROM Event e WHERE e.type.name = :typeName")
    List<Event> findByTypeName(@Param("typeName") String typeName);

    // Buscar eventos en un rango de fechas
    @Query("SELECT e FROM Event e WHERE e.eventDate BETWEEN :startDate AND :endDate ORDER BY e.eventDate ASC")
    List<Event> findEventsBetweenDates(@Param("startDate") LocalDateTime startDate, @Param("endDate") LocalDateTime endDate);

    // Buscar eventos a los que asiste un usuario específico
    @Query("SELECT e FROM Event e JOIN e.attendees a WHERE a.username = :username ORDER BY e.eventDate DESC")
    List<Event> findEventsByAttendeeUsername(@Param("username") String username);

    // Verificar si un usuario ya está registrado para asistir a un evento
    @Query("SELECT COUNT(e) > 0 FROM Event e JOIN e.attendees a WHERE e.id = :eventId AND a.username = :username")
    boolean isUserRegisteredForEvent(@Param("eventId") Long eventId, @Param("username") String username);

    // Obtener la cantidad de asistentes de un evento
    @Query("SELECT SIZE(e.attendees) FROM Event e WHERE e.id = :eventId")
    int getAttendeesCount(@Param("eventId") Long eventId);

    // Buscar eventos con estado específico a los que asiste un usuario
    @Query("SELECT e FROM Event e JOIN e.attendees a WHERE a.username = :username AND e.status = :status ORDER BY e.eventDate DESC")
    List<Event> findEventsByAttendeeUsernameAndStatus(@Param("username") String username, @Param("status") EventStatus status);
}
