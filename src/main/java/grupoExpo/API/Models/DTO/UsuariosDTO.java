package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode
@Getter @Setter
public class UsuariosDTO {

    private String idUsuario;

    @NotBlank(message = "El rol es obligatorio")
    private String idRol;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreUsuario;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo valido")
    private String correoUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contraseñaUsuario;

    @NotBlank(message = "La respuesta de seguridad es obligatoria")
    private String segurityAnswerUsuario;

    private String imagenUsuario;

    @NotNull(message = "El genero es obligatorio")
    private char generoUsuario;
}
