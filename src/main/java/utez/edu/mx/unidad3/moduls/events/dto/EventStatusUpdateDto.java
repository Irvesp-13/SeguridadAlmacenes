package utez.edu.mx.unidad3.moduls.events.dto;

import jakarta.validation.constraints.NotNull;
import utez.edu.mx.unidad3.moduls.events.EventStatus;

public class EventStatusUpdateDto {

    @NotNull(message = "El estado del evento es obligatorio")
    private EventStatus status;

    // Constructor vacío
    public EventStatusUpdateDto() {}

    // Constructor con parámetros
    public EventStatusUpdateDto(EventStatus status) {
        this.status = status;
    }

    // Getters y Setters
    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }
}
