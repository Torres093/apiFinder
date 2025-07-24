package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString @EqualsAndHashCode
@Getter @Setter
public class ReseñasDTO {

    private String idReseña;

    @NotBlank(message = "El cliente es obligatorio")
    private String idCliente;

    @NotBlank(message = "El hotel es obligatorio")
    private String IdHotel;

    @NotBlank(message = "El comentario es obligatorio")
    private String comentarioReseña;

    @NotNull(message = "La calificacion es obligatoria")
    @Min(value = 0, message = "El valor debe ser mayor o igual a 0")
    @Max(value = 5, message = "El valor debe ser menor o igual a 5")
    private int calificacionReseña;
}
