package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@ToString @EqualsAndHashCode
@Getter @Setter
public class CheckOutsDTO {

    private String idCheckOut;

    @NotBlank(message = "El detalle de la reserva es obligatorio")
    private String idDetalle;

    @NotBlank(message = "El empleado es obligatorio")
    private String idEmpleado;

    @NotNull(message = "La fecha y hora es obligatoria")
    private LocalDateTime fechaYHoraCheckOut;

    private String observacionCheckOut;
}
