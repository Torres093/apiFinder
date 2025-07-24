package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.UUID;

@ToString @EqualsAndHashCode
@Getter @Setter
public class HabitacionesDTO {
    private String idHabitacion;

    @NotBlank(message = "El Tipo de Habitacion es obligatorio")
    private String idTipoHabitacion;

    @NotBlank(message = "El hotel es obligatorio")
    private String idHotel;

    @NotBlank(message = "El estado del hotel es obligatorio")
    private String idEstadoHabitacion;

    @NotNull(message = "EL numero de habitaciones es obligatorio")
    private int numeroHabitacion;

    private String descripcionHabitacion;

    @NotNull(message = "El precio de la habitacion es obligatorio")
    private double precioHabitacion;

    @NotNull(message = "El numero de la capacidad es obligatorio")
    private int capacidadHabitacion;

}
