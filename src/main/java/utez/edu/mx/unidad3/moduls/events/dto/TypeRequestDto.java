package utez.edu.mx.unidad3.moduls.events.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public class TypeRequestDto {

    @NotNull(message = "El nombre del tipo es obligatorio")
    @NotBlank(message = "El nombre del tipo no puede estar vacío")
    @Size(min = 3, max = 50, message = "El nombre del tipo debe tener entre 3 y 50 caracteres")
    private String name;

    @Size(max = 255, message = "La descripción no puede exceder 255 caracteres")
    private String description;

    // Constructor vacío
    public TypeRequestDto() {}

    // Constructor completo
    public TypeRequestDto(String name, String description) {
        this.name = name;
        this.description = description;
    }

    // Getters y Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
