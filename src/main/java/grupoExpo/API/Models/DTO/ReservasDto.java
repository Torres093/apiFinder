package grupoExpo.API.Models.DTO;


import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter@Setter@NotBlank
public class ReservasDto {

    private Long idReserva;
    private Long idCliente;
    private Long idEstadoReserva;
    private Long idMetodoPago;
    private Date fechaReserva;
    private double precioTotalReserva;
}
