package utez.edu.mx.unidad3.moduls.groups;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.unidad3.utils.APIResponse;
import utez.edu.mx.unidad3.moduls.user.UserRepository;
import utez.edu.mx.unidad3.moduls.user.User;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
public class GroupService {
    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    // Consultar todos los grupos
    @Transactional(readOnly = true)
    public APIResponse findAll() {
        List<Group> list = new ArrayList<>();
        list = groupRepository.findAll();

        return new APIResponse("Operacion exitosa", list, false, HttpStatus.OK);
    }

    @Transactional(readOnly = true)
    public APIResponse findById(Long id) {
        try {
            Group found = groupRepository.findById(id).orElse(null);
            if (found == null) {
                return new APIResponse("El grupo no existe", true, HttpStatus.BAD_REQUEST);
            }
            return new APIResponse("Operacion exitosa", found,  false, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse("No se pudo consultar al grupo", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Crear un grupo
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse saveGroup(Group payload) {
        try {
            if (groupRepository.findByName((payload.getName())).isPresent()) {
                return new APIResponse("El nombre ya existe", true, HttpStatus.BAD_REQUEST);
            }
            // Adjuntar usuarios existentes
            if (payload.getUsers() != null && !payload.getUsers().isEmpty()) {
                List<User> attachedUsers = new ArrayList<>();
                for (User user : payload.getUsers()) {
                    if (user.getId() != null) {
                        User attached = userRepository.findById(user.getId())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + user.getId()));
                        attached.setGroup(payload); // Asegura la relación bidireccional
                        attachedUsers.add(attached);
                    }
                }
                payload.setUsers(attachedUsers);
            }
            groupRepository.save(payload);
            return new APIResponse ("Operacion exitosa", false, HttpStatus.CREATED);
        } catch (Exception ex) {
            // Loguear el error si es necesario
            throw ex; // Relanzar la excepción para que Spring maneje el rollback correctamente
        }
    }

    // Actualizar un grupo
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse updateGroup(Group payload) {
        try {
            Group found = groupRepository.findById(payload.getId()).orElse(null);
            if (found == null) {
                return new APIResponse("El grupo no existe", true, HttpStatus.BAD_REQUEST);
            }

            // Verificar si el nombre pertenece a otro grupo
            Group nameOwner = groupRepository.findByName(payload.getName()).orElse(null);
            if (nameOwner != null && !nameOwner.getId().equals(payload.getId())) {
                return new APIResponse("El nombre ya está en uso por otro grupo", true, HttpStatus.BAD_REQUEST);
            }

            // Actualizar el administrador del grupo si se envía en el payload
            if (payload.getAdminUser() != null && payload.getAdminUser().getId() != null) {
                User admin = userRepository.findById(payload.getAdminUser().getId())
                        .orElseThrow(() -> new RuntimeException("Administrador no encontrado con id: " + payload.getAdminUser().getId()));
                found.setAdminUser(admin);
            }

            // Actualizar solo los campos del grupo
            found.setName(payload.getName());
            found.setMunicipio(payload.getMunicipio());
            found.setColonia(payload.getColonia());

            // Si se envía una lista de usuarios, solo actualizar la relación (opcional)
            if (payload.getUsers() != null && !payload.getUsers().isEmpty()) {
                // Solo asociar usuarios existentes, sin sobrescribir datos críticos
                List<User> attachedUsers = new ArrayList<>();
                for (User user : payload.getUsers()) {
                    if (user.getId() != null) {
                        User attached = userRepository.findById(user.getId())
                                .orElseThrow(() -> new RuntimeException("Usuario no encontrado con id: " + user.getId()));
                        attached.setGroup(found); // Asegura la relación bidireccional
                        attachedUsers.add(attached);
                    }
                }
                found.setUsers(attachedUsers);
            }

            groupRepository.save(found);
            return new APIResponse("Operacion exitosa", false, HttpStatus.OK);
        } catch (NullPointerException nullex) {
            nullex.printStackTrace();
            return new APIResponse("No se aceptan valores nulos", true, HttpStatus.BAD_REQUEST);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse("Error al actualizar el grupo", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Eliminar un grupo
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse deleteGroup(Group payload) {
        try {
            Group found = groupRepository.findById(payload.getId()).orElse(null);
            if (found == null) {
                return new APIResponse("El grupo no existe", true, HttpStatus.BAD_REQUEST);
            }
            groupRepository.deleteById(found.getId());
            return new APIResponse("Operacion exitosa", false, HttpStatus.OK);
        } catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse("Error al eliminar el grupo", true, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
