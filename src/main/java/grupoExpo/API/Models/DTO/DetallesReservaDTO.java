package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString @EqualsAndHashCode
@Getter @Setter
public class DetallesReservaDTO {

    private String idDetalle;

    @NotBlank(message = "La reserva es obligatoria")
    private String idReserva;

    @NotBlank(message = "La habitacion es obligatoria")
    private String idHabitacion;

    @NotNull(message = "El numero de huespedes es obligatorio")
    private int numeroHuespedesDetalle;

    @NotNull(message = "La fecha y hora de llegada es obligatoria")
    private LocalDateTime fechaYHoraDeLlegadaDetalle;

    @NotNull(message = "La fecha y hora de salida es obligatoria")
    private LocalDateTime fechaYHoraDeSalidaDetalle;

    @NotNull(message = "El precio es obligatorio")
    @Digits(integer = 5, fraction = 2, message = "El valor debe tener como maximo 5 digitos enteros y 2 decimales")
    private double precioDetalle;

    @NotNull(message = "El descuento es obligatorio")
    @Min(value = 0, message = "El valor debe ser mayor o igual a 0")
    @Max(value = 100, message = "El valor debe ser menor o igual a 100")
    private int descuentoDetalle;

    //Campos adicionales
    private int numeroHabitacion;
}
