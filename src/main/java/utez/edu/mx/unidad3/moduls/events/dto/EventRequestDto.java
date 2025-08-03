package utez.edu.mx.unidad3.moduls.events.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.time.LocalDateTime;

public class EventRequestDto {

    @NotNull(message = "El título es obligatorio")
    @NotBlank(message = "El título no puede estar vacío")
    @Size(min = 3, max = 100, message = "El título debe tener entre 3 y 100 caracteres")
    private String title;

    @NotNull(message = "La fecha del evento es obligatoria")
    private LocalDateTime eventDate;

    @NotNull(message = "El tipo de evento es obligatorio")
    @NotBlank(message = "El tipo de evento no puede estar vacío")
    @Size(min = 3, max = 50, message = "El tipo de evento debe tener entre 3 y 50 caracteres")
    private String eventType;

    @Size(max = 500, message = "La descripción no puede exceder 500 caracteres")
    private String description;

    @Size(max = 255, message = "La ubicación no puede exceder 255 caracteres")
    private String location;

    // Constructor vacío
    public EventRequestDto() {}

    // Constructor completo
    public EventRequestDto(String title, LocalDateTime eventDate, String eventType, String description, String location) {
        this.title = title;
        this.eventDate = eventDate;
        this.eventType = eventType;
        this.description = description;
        this.location = location;
    }

    // Getters y Setters
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
}
