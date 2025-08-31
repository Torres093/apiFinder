package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode
@Getter @Setter
public class PlatosDTO {

    private String idPlato;

    @NotBlank(message = "El hotel es obligatorio")
    private String idHotel;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombrePlato;

    private String descripcionPlato;

    @NotNull(message = "El precio es obligatorio")
    @Digits(integer = 8, fraction = 2, message = "El valor debe tener como maximo 8 digitos enteros y 2 decimales")
    private double precioPlato;
}
