package utez.edu.mx.unidad3.moduls.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.unidad3.moduls.auth.dto.LoginRequestDTO;
import utez.edu.mx.unidad3.moduls.user.Rol;
import utez.edu.mx.unidad3.moduls.user.RolRepository;
import utez.edu.mx.unidad3.moduls.user.User;
import utez.edu.mx.unidad3.moduls.user.UserRepository;
import utez.edu.mx.unidad3.security.jwt.JWTUtils;
import utez.edu.mx.unidad3.security.jwt.UDService;
import utez.edu.mx.unidad3.utils.APIResponse;
import utez.edu.mx.unidad3.utils.PasswordEncoder;

import java.sql.SQLException;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RolRepository rolRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UDService uDService;

    @Transactional(readOnly = true)
    public APIResponse doLogin(LoginRequestDTO payload) {
        try {
            User found = userRepository.findByUsername(
                    payload.getUsername()
            ).orElse(null);

            if (found == null) return new APIResponse("Usuario no encontrado", true, HttpStatus.NOT_FOUND);

            if (! PasswordEncoder.verifyPassword(payload.getPassword(), found.getPassword()))
                return new APIResponse("Las contraseñas no coinciden", true, HttpStatus.BAD_REQUEST);

            UserDetails userDetails = uDService.loadUserByUsername(found.getUsername());
            String token = jwtUtils.generateToken(userDetails);
            return new APIResponse(
                    "Opreacion exitosa",
                    token,
                    false,
                    HttpStatus.OK
            );
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse(
                    "Error al iniciar sesión",
                    true,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
    @Transactional(rollbackFor = {SQLException.class, Exception.class})
    public APIResponse register(User payload) {
        try {
            if (userRepository.findByUsername(payload.getUsername()).orElse(null) != null)
                return new APIResponse(
                        "El nombre de usuario ya existe",
                        true,
                        HttpStatus.BAD_REQUEST
                );

            // Buscar o crear el rol
            Rol rol = null;
            if (payload.getRol() != null) {
                if (payload.getRol().getId() != null) {
                    // Buscar el rol por ID si se proporciona
                    rol = rolRepository.findById(payload.getRol().getId()).orElse(null);
                } else if (payload.getRol().getName() != null) {
                    // Buscar el rol por nombre si se proporciona
                    rol = rolRepository.findByName(payload.getRol().getName()).orElse(null);
                    if (rol == null) {
                        // Crear un nuevo rol si no existe
                        rol = new Rol();
                        rol.setName(payload.getRol().getName());
                        rol = rolRepository.save(rol);
                    }
                }
            }

            if (rol == null) {
                return new APIResponse(
                        "El rol especificado no existe",
                        true,
                        HttpStatus.BAD_REQUEST
                );
            }

            payload.setRol(rol);
            payload.setPassword(PasswordEncoder.encodePassword(payload.getPassword()));
            userRepository.save(payload);

            return new APIResponse(
                    "Operacion exitosa",
                    false,
                    HttpStatus.CREATED
            );
        }
        catch (Exception ex) {
            ex.printStackTrace();
            return new APIResponse(
                    "Error al registrar usuario",
                    true,
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
