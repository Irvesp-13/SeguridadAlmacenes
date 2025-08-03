package utez.edu.mx.unidad3.moduls.groups;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import utez.edu.mx.unidad3.moduls.user.User;

import java.util.List;

@Entity
@Table(name = "grupo")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "name", nullable = false)
    @Pattern(regexp = "^[A-Za-z]{1}[\\sA-Za-z]{5,}$", message = "Favor de colocar unicamente letras y espacios")
    @NotNull(message = "Favor de ingresar un valor")
    @NotBlank(message = "No se aceptan valores en blanco")
    private String name;

    @Pattern(regexp = "^[A-Za-z]{1}[\\sA-Za-z]{5,}$", message = "Solo aceptamos letras")
    @NotNull(message = "Favor de ingresar un valor")
    @NotBlank(message = "No se aceptan valores en blanco")
    @Column(name = "municipio", nullable = false)
    private String municipio;

    @Pattern(regexp = "^[A-Za-z]{1}[\\sA-Za-z]{5,}$", message = "Solo aceptamos letras")
    @NotNull(message = "Favor de ingresar un valor")
    @NotBlank(message = "No se aceptan valores en blanco")
    @Column(name = "colonia", nullable = false)
    private String colonia;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private List<User> users;

    @OneToOne
    @JoinColumn(name = "admin_user_id", unique = true)
    private User adminUser;

    public Group() {
    }

    public Group(Long id, String name, String municipio, String colonia, List<User> users, User adminUser) {
        this.id = id;
        this.name = name;
        this.municipio = municipio;
        this.colonia = colonia;
        this.users = users;
        this.adminUser = adminUser;
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

    public String getMunicipio() {
        return municipio;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public String getColonia() {
        return colonia;
    }

    public void setColonia(String colonia) {
        this.colonia = colonia;
    }

    public List<User> getUsers() {
        return users;
    }

    public void setUsers(List<User> users) {
        this.users = users;
    }

    public User getAdminUser() {
        return adminUser;
    }

    public void setAdminUser(User adminUser) {
        this.adminUser = adminUser;
    }
}
