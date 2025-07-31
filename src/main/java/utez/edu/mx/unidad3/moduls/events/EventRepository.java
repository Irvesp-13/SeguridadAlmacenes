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

    // Buscar eventos por grupo (ID)
    List<Event> findByGroupId(Long groupId);

    // Buscar eventos por nombre de grupo
    @Query("SELECT e FROM Event e WHERE e.group.name = :groupName ORDER BY e.eventDate DESC")
    List<Event> findByGroupName(@Param("groupName") String groupName);

    // Buscar evento por título y nombre de grupo (el más reciente si hay múltiples)
    @Query("SELECT e FROM Event e WHERE e.title = :title AND e.group.name = :groupName ORDER BY e.createdAt DESC LIMIT 1")
    Optional<Event> findByTitleAndGroupName(@Param("title") String title, @Param("groupName") String groupName);

    // Buscar todos los eventos con el mismo título y grupo
    @Query("SELECT e FROM Event e WHERE e.title = :title AND e.group.name = :groupName ORDER BY e.createdAt DESC")
    List<Event> findAllByTitleAndGroupName(@Param("title") String title, @Param("groupName") String groupName);

    // Buscar eventos por estado
    List<Event> findByStatus(EventStatus status);

    // Buscar eventos por grupo y estado usando nombre de grupo
    @Query("SELECT e FROM Event e WHERE e.group.name = :groupName AND e.status = :status")
    List<Event> findByGroupNameAndStatus(@Param("groupName") String groupName, @Param("status") EventStatus status);

    // Buscar eventos próximos (fecha futura)
    @Query("SELECT e FROM Event e WHERE e.eventDate > :currentDate ORDER BY e.eventDate ASC")
    List<Event> findUpcomingEvents(@Param("currentDate") LocalDateTime currentDate);

    // Buscar eventos de un grupo ordenados por fecha (usando ID)
    @Query("SELECT e FROM Event e WHERE e.group.id = :groupId ORDER BY e.eventDate DESC")
    List<Event> findByGroupIdOrderByEventDateDesc(@Param("groupId") Long groupId);

    // Buscar eventos por tipo
    List<Event> findByEventTypeContainingIgnoreCase(String eventType);

    // Buscar eventos en un rango de fechas
    @Query("SELECT e FROM Event e WHERE e.eventDate BETWEEN :startDate AND :endDate ORDER BY e.eventDate ASC")
    List<Event> findEventsBetweenDates(@Param("startDate") LocalDateTime startDate,
                                      @Param("endDate") LocalDateTime endDate);
}
