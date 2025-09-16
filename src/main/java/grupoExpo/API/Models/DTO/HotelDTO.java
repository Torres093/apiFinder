package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.util.Date;

@ToString @EqualsAndHashCode
@Getter @Setter
public class HotelDTO {

    private String IdHotel;

    @NotBlank(message = "El Tipo de hotel es obligatorio")
    private String IdTipoHotel;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombreHotel;

    @NotBlank(message = "La ubicacion del hotel es obligatoria")
    private String ubicacionHotel;

    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe de ser un correo valido")
    private String correoHotel;

    @NotNull(message = "La fecha de creacion es obligatoria")
    private LocalDate fechaCreacionHotel;

    @NotBlank(message = "El telefono es obligatorio")
    private String telefonoHotel;

    private String imagenHotel;

    @NotNull(message = "El numero de habitaciones es obligatorio")
    private int numeroHabitacionesHotel;

    //Campos adicionales
    private String nombreTipoHotel;
}
