package utez.edu.mx.unidad3.moduls.events.dto;

import utez.edu.mx.unidad3.moduls.events.EventStatus;

import java.time.LocalDateTime;

public class EventResponseDto {

    private Long id;
    private String title;
    private LocalDateTime eventDate;
    private String eventType;
    private EventStatus status;
    private String statusDisplayName;

    // Información del creador
    private Long creatorId;
    private String creatorUsername;
    private String creatorFullName;

    // Nuevos campos
    private String description;
    private String location;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // Constructor vacío
    public EventResponseDto() {}

    // Constructor completo
    public EventResponseDto(Long id, String title, LocalDateTime eventDate, String eventType,
                           EventStatus status, Long creatorId, String creatorUsername,
                           String creatorFullName, String description, String location,
                           LocalDateTime createdAt, LocalDateTime updatedAt) {
        this.id = id;
        this.title = title;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.status = status;
        this.statusDisplayName = status.getDisplayName();
        this.creatorId = creatorId;
        this.creatorUsername = creatorUsername;
        this.creatorFullName = creatorFullName;
        this.description = description;
        this.location = location;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public LocalDateTime getEventDate() {
        return eventDate;
    }

    public void setEventDate(LocalDateTime eventDate) {
        this.eventDate = eventDate;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
        this.statusDisplayName = status != null ? status.getDisplayName() : null;
    }

    public String getStatusDisplayName() {
        return statusDisplayName;
    }

    public void setStatusDisplayName(String statusDisplayName) {
        this.statusDisplayName = statusDisplayName;
    }

    public Long getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Long creatorId) {
        this.creatorId = creatorId;
    }

    public String getCreatorUsername() {
        return creatorUsername;
    }

    public void setCreatorUsername(String creatorUsername) {
        this.creatorUsername = creatorUsername;
    }

    public String getCreatorFullName() {
        return creatorFullName;
    }

    public void setCreatorFullName(String creatorFullName) {
        this.creatorFullName = creatorFullName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
