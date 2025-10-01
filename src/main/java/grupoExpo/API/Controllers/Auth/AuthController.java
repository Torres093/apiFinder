package grupoExpo.API.Controllers.Auth;

import grupoExpo.API.Config.Argon2Password;
import grupoExpo.API.Entities.Usuarios.UsuariosEntity;
import grupoExpo.API.Models.DTO.LoginDTO;
import grupoExpo.API.Models.DTO.UsuariosDTO;
import grupoExpo.API.Repositories.Usuarios.UsuariosRepository;
import grupoExpo.API.Services.Auth.AuthService;
import grupoExpo.API.Utils.JWTUtils;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class AuthController {

    @Autowired
    private AuthService service;

    @Autowired
    private JWTUtils jwtUtils;

    @PostMapping("/authLogin")
    private ResponseEntity<String> login(@Valid @RequestBody LoginDTO data, HttpServletResponse response){
        //1. Verificar que los datos no estén vacíos
        if (data.getCorreoUsuario() == null || data.getCorreoUsuario().isBlank() || data.getCorreoUsuario().isEmpty() ||
                data.getContraseñaUsuario() == null || data.getContraseñaUsuario().isBlank() || data.getContraseñaUsuario().isEmpty()){
            return ResponseEntity.status(401).body("Error: Credenciales incompletas");
        }

        //2. Enviar los datos al metodo login contenido en el service
        if (service.Login(data.getCorreoUsuario(), data.getContraseñaUsuario())){
            addTokenCookie(response, data.getCorreoUsuario());
            return ResponseEntity.ok("Inicio de sesion exitoso");
        }
        return ResponseEntity.status(401).body("Credenciales incorrectas");
    }

    /**
     * Se genera el token y se guarda en la Cookie
     * @param response
     * @param
     */
    private void addTokenCookie(HttpServletResponse response, String correo) {
        // Obtener el usuario completo de la base de datos
        Optional<UsuariosEntity> userOpt = service.obtenerUsuario(correo);

        if (userOpt.isPresent()) {
            UsuariosEntity user = userOpt.get();
            String token = jwtUtils.create(
                    String.valueOf(user.getIdUsuario()),
                    user.getCorreoUsuario(),
                    user.getRol().getNombreRol() // ← Usar el nombre real del tipo
            );

            Cookie cookie = new Cookie("authToken", token);
            cookie.setHttpOnly(true);
            cookie.setSecure(true);
            cookie.setPath("/");
            cookie.setMaxAge(900);
            response.addCookie(cookie);
        }
    }

    @GetMapping("/authMe")
    public ResponseEntity<?> getCurrentUser(Authentication authentication) {
        try {
            if (authentication == null || !authentication.isAuthenticated()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body(Map.of(
                                "authenticated", false,
                                "message", "No autenticado"
                        ));
            }

            // Manejar diferentes tipos de Principal
            String username;
            Collection<? extends GrantedAuthority> authorities;

            if (authentication.getPrincipal() instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) authentication.getPrincipal();
                username = userDetails.getUsername();
                authorities = userDetails.getAuthorities();
            } else {
                username = authentication.getName();
                authorities = authentication.getAuthorities();
            }

            Optional<UsuariosEntity> userOpt = service.obtenerUsuario(username);

            if (userOpt.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(Map.of(
                                "authenticated", false,
                                "message", "Usuario no encontrado"
                        ));
            }

            UsuariosEntity user = userOpt.get();

            return ResponseEntity.ok(Map.of(
                    "authenticated", true,
                    "user", Map.of(
                            "id", user.getIdUsuario(),
                            "rol", user.getRol().getNombreRol(),
                            "nombre", user.getNombreUsuario(),
                            "correo", user.getCorreoUsuario(),
                            "contraseña", user.getContraseñaUsuario(),
                            "segurityAnswer", user.getSegurityAnswerUsuario(),
                            "imagen", user.getImagenUsuario(),
                            "genero", user.getGeneroUsuario(),
                            "authorities", authorities.stream()
                                    .map(GrantedAuthority::getAuthority)
                                    .collect(Collectors.toList())
                    )
            ));

        } catch (Exception e) {
            //log.error("Error en /me endpoint: ", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of(
                            "authenticated", false,
                            "message", "Error obteniendo datos de usuario"
                    ));
        }
    }
}
