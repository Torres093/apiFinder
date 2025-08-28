package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;

@ToString @EqualsAndHashCode
@Getter @Setter
public class EventosDTO {

    private String idEvento;

    @NotBlank(message = "EL hotel es obligatorio")
    private String idHotel;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreEvento;

    private String descripcionEvento;

    @NotNull(message = "La fecha es obligatoria")
    private LocalDate fechaEvento;

    @NotNull(message = "La capacidad del evento es obligatoria")
    private int capacidadEvento;

    @NotNull(message = "El precio es obligatorio")
    @Digits(integer = 8, fraction = 2, message = "El valor debe tener como maximo 8 digitos enteros y 2 decimales")
    private double precioEvento;
}
