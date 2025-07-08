package utez.edu.mx.unidad3.moduls.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import utez.edu.mx.unidad3.moduls.auth.dto.LoginRequestDTO;
import utez.edu.mx.unidad3.moduls.user.User;
import utez.edu.mx.unidad3.moduls.user.UserRepository;
import utez.edu.mx.unidad3.security.jwt.JWTUtils;
import utez.edu.mx.unidad3.security.jwt.UDService;
import utez.edu.mx.unidad3.utils.APIResponse;

@Service
public class AuthService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JWTUtils jwtUtils;

    @Autowired
    private UDService uDService;

    @Transactional(readOnly = true)
    public APIResponse doLogin(LoginRequestDTO payload) {
        try {
            User found = userRepository.findByUsernameAndPassword(
                    payload.getUsername(),
                    payload.getPassword()
            ).orElse(null);

            if (found == null) return new APIResponse("Usuario no encontrado", true, HttpStatus.NOT_FOUND);

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
}
