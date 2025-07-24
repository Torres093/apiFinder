package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.util.Date;
import java.util.UUID;

@ToString @EqualsAndHashCode
@Getter @Setter
public class ClientesDTO {

    private String idCliente;

    @NotBlank(message = "El Usuario es obligatorio")
    private String idUsuario;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreCliente;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidoCliente;

    @NotBlank(message = "El dui es obligatorio")
    private String duiCliente;

    //@NotBlank(message = "La fecha de nacimiento es obligatoria")
    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private Date nacimientoCliente;

}
