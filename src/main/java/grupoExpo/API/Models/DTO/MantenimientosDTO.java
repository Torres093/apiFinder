package grupoExpo.API.Models.DTO;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDate;
import java.time.LocalTime;

@ToString @EqualsAndHashCode
@Getter @Setter
public class MantenimientosDTO {

    private String idMantenimiento;

    @NotBlank(message = "La habitacion es obligatoria")
    private String idHabitacion;

    @NotBlank(message = "El empleado es obligatorio")
    private String idEmpleado;

    @NotBlank(message = "El tipoMantenimiento es obligatorio")
    private String idTipoMantenimiento;

    @NotNull(message = "La fecha de mantenimiento es obligatoria")
    private LocalDate fechaMantenimiento;

    @NotNull(message = "La hora de inicio del mantenimiento es obligatoria")
    private LocalTime horaInicioMantenimiento;

    @NotNull(message = "La hora de fin del mantenimiento es obligatoria")
    private LocalTime horaFinMantenimiento;

    private String observacionMantenimiento;

    //Campos adicionales
    private int numeroHabitacion;

    private String nombreEmpleado;

    private String nombreTipoMantenimiento;
}
