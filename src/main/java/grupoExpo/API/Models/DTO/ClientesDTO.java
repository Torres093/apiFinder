package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@ToString @EqualsAndHashCode
@Getter @Setter
public class ClientesDTO {

    private byte[] idCliente;

    private byte[] idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreCliente;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidoCliente;

    @NotBlank(message = "El dui es obligatorio")
    private String duiCliente;

    @NotBlank(message = "La fecha de nacimiento es obligatoria")
    private Date nacimientoCliente;

}
