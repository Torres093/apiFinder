package grupoExpo.API.Models.DTO;


import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;

@Getter @Setter
@ToString @EqualsAndHashCode
public class ReservasDTO {

    private String idReserva;

    @NotBlank(message = "El cliente es obligatorio")
    private String idCliente;

    @NotBlank(message = "El estado de la reserva es obligatorio")
    private String idEstadoReserva;

    @NotBlank(message = "El metodo de pago es obligatorio")
    private String idMetodoPago;

    @NotNull(message = "La fecha de la reserva es obligatoria")
    private LocalDate fechaReserva;

    @NotNull(message = "El precio total de la reserva es obligatorio")
    @Digits(integer = 12, fraction = 2, message = "El valor debe tener como maximo 12 digitos enteros y 2 decimales")
    private double precioTotalReserva;
}
