package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode
@Getter @Setter
public class DetallesServicioEventoDTO {

    private String idDetalleServicioEvento;

    @NotBlank(message = "El servicio es obligatorio")
    private String idServicio;

    @NotBlank(message = "El evento es obligatorio")
    private String IdEvento;

    //Campos adicionales
    private String nombreServicio;

    private String nombreEvento;
}
