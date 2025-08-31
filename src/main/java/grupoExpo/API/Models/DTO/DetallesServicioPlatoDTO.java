package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode
@Getter @Setter
public class DetallesServicioPlatoDTO {

    private String idDetalleServicioPlato;

    @NotBlank(message = "El servicio es obligatorio")
    private String idServicio;

    @NotBlank(message = "El plato es obligatorio")
    private String idPlato;
}
