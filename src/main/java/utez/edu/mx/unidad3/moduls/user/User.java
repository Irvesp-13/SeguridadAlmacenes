package utez.edu.mx.unidad3.moduls.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import utez.edu.mx.unidad3.moduls.groups.Group;
import utez.edu.mx.unidad3.moduls.events.Event;

import java.util.Set;

@Entity
@Table(name = "user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @ManyToOne
    @JoinColumn(name = "rol_id", nullable = false)
    private Rol rol;

    @Column(name = "nombre_completo", nullable = false)
    private String nombreCompleto;

    @Column(name = "telefono", nullable = false)
    private String telefono;

    @Column(name = "correo", nullable = false)
    private String correo;

    @ManyToOne
    @JoinColumn(name = "group_id")
    @JsonIgnore
    private Group group;

    @OneToOne(mappedBy = "adminUser")
    @JsonIgnore
    private Group groupAdmin;

    // Eventos creados por este usuario
    @OneToMany(mappedBy = "creator", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JsonIgnore
    private Set<Event> createdEvents;

    // Relación Muchos a Muchos con Event (eventos a los que asistirá)
    @ManyToMany
    @JoinTable(
        name = "user_event",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "event_id")
    )
    @JsonIgnore
    private Set<Event> events; // Eventos a los que asistirá este usuario

    public User() {
    }

    public User(Long id, String username, String password, Rol rol, String nombreCompleto, String telefono, String correo, Group group, Group groupAdmin) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.rol = rol;
        this.nombreCompleto = nombreCompleto;
        this.telefono = telefono;
        this.correo = correo;
        this.group = group;
        this.groupAdmin = groupAdmin;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Rol getRol() {
        return rol;
    }

    public void setRol(Rol rol) {
        this.rol = rol;
    }

    public String getNombreCompleto() {
        return nombreCompleto;
    }

    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public Group getGroup() {
        return group;
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    public Group getGroupAdmin() {
        return groupAdmin;
    }

    public void setGroupAdmin(Group groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public Set<Event> getCreatedEvents() {
        return createdEvents;
    }

    public void setCreatedEvents(Set<Event> createdEvents) {
        this.createdEvents = createdEvents;
    }

    public Set<Event> getEvents() {
        return events;
    }

    public void setEvents(Set<Event> events) {
        this.events = events;
    }
}
