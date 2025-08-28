package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString @EqualsAndHashCode
@Getter @Setter
public class EmpleadosDTO {

    private String idEmpleado;

    @NotBlank(message = "El usuario es obligatorio")
    private String idUsuario;

    @NotBlank(message = "El cargo es obligatorio")
    private String idCargo;

    @NotBlank(message = "El hotel es obligatorio")
    private String idHotel;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreEmpleado;

    @NotBlank(message = "El apellido es obligatorio")
    private String apellidoEmpleado;

    @NotBlank(message = "La dirección es obligatoria")
    private String direccionEmpleado;

    @NotNull(message = "La fecha de nacimiento es obligatoria")
    private LocalDate nacimientoEmpleado;

    @NotBlank(message = "El número de teléfono es obligatorio")
    private String telefonoEmpleado;

    @NotNull(message = "El salario es obligatorio")
    @Digits(integer = 5, fraction = 2, message = "El valor debe tener como maximo 5 digitos enteros y 2 decimales")
    private double salarioEmpleado;

    @NotBlank(message = "El DUI es obligatorio")
    private String duiEmpleado;
}
