package grupoExpo.API.Models.DTO;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

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
    private Date fechaReserva;

    @NotNull(message = "El precio total de la reserva es obligatorio")
    private double precioTotalReserva;
}
