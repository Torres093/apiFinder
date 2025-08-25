package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode
@Getter @Setter
public class ServiciosDTO {

    private String idServicio;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreServicio;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcionServicio;
}
