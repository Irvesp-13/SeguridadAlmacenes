package utez.edu.mx.unidad3.moduls.clients;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import utez.edu.mx.unidad3.moduls.warehouse.Warehouse;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @Pattern(regexp = "^[A-Za-z]{1}[\\sA-Za-z]{5,}$", message = "Favor de colocar unicamente letras y espacios")
    @NotNull(message = "Favor de ingresar un valor")
    @NotBlank(message = "No se aceptan valores en blanco")
    private String name;

    @Column(name = "email", nullable = false, unique = true)
    @Pattern(regexp = "^[a-z0-9][a-z0-9_.]{3,}@[a-z]{2,}(\\.[a-z]{2,}){1,2}$", message = "Favor de colocar un correo electrónico válido")
    @NotNull(message = "Favor de ingresar un valor")
    @NotBlank(message = "No se aceptan valores en blanco")
    private String email;

    @Column(name = "phone", nullable = false)
    @Pattern(regexp = "^[0-9]{10}$", message = "Favor de colocar un número de teléfono válido")
    @NotNull(message = "Favor de ingresar un valor")
    @NotBlank(message = "No se aceptan valores en blanco")
    private String phone;

    @OneToMany(mappedBy = "client")
    @JsonIgnore
    private List<Warehouse> warehouse;

    public Client() {}

    public Client(Long id, String name, String email, String phone, List<Warehouse> warehouse) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.warehouse = warehouse;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<Warehouse> getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(List<Warehouse> warehouse) {
        this.warehouse = warehouse;
    }
}
