package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@EqualsAndHashCode
public class LoginDTO {

    private String idUsuario;

    private String idRol;

    private String nombreUsuario;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ser un correo valido")
    private String correoUsuario;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
    private String contraseñaUsuario;

    private String segurityAnswerUsuario;

    private String imagenUsuario;

    private char generoUsuario;

    //Campos adicionales
    private String nombreRol;
}
