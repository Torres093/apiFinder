package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode
@Getter @Setter
public class DetallesReservaServicioDTO {

    private String idDetalleReservaServicio;

    @NotBlank(message = "La reserva es obligatoria")
    private String idReserva;

    @NotBlank(message = "El servicio es obligatorio")
    private String idServicio;

    //Campos adicionales
    private String nombreServicio;
}
