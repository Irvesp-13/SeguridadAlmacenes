package utez.edu.mx.unidad3.moduls.events.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EventIdentifierDto {

    @NotNull(message = "El título del evento es obligatorio")
    @NotBlank(message = "El título del evento no puede estar vacío")
    private String title;

    @NotNull(message = "El nombre del grupo es obligatorio")
    @NotBlank(message = "El nombre del grupo no puede estar vacío")
    private String groupName;

    // Constructor vacío
    public EventIdentifierDto() {}

    // Constructor con parámetros
    public EventIdentifierDto(String title, String groupName) {
        this.title = title;
        this.groupName = groupName;
    }

    // Getters y Setters
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }
}
