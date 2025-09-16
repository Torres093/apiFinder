package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode
@Getter @Setter
public class TiposHabitacionDTO {

    private String idTipoHabitacion;

    @NotBlank(message = "La categoria del tipo de habitacion es obligatoria")
    private String idCategoriaTipoHabitacion;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreTipoHabitacion;

    @NotBlank(message = "La descripcion es obligatoria")
    private String descripcionTipoHabitacion;

    //Campos adicionales
    private String nombreCategoriaTipoHabitacion;
}
