package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode
@Getter @Setter
public class CargosDTO {

    private String idCargo;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreCargo;
}
