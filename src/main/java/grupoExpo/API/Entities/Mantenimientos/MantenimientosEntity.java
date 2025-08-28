package grupoExpo.API.Entities.Mantenimientos;

import grupoExpo.API.Entities.Empleados.EmpleadosEntity;
import grupoExpo.API.Entities.Habitaciones.HabitacionesEntity;
import grupoExpo.API.Entities.TiposMantenimiento.TiposMantenimientoEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name = "MANTENIMIENTOS")
@Getter @Setter @ToString @EqualsAndHashCode
public class MantenimientosEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDMANTENIMIENTO")
    private String idMantenimiento;

    @ManyToOne
    @JoinColumn(name = "IDHABITACION", referencedColumnName = "IDHABITACION", columnDefinition = "RAW(16)")
    private HabitacionesEntity Habitacion;

    @ManyToOne
    @JoinColumn(name = "IDEMPLEADO", referencedColumnName = "IDEMPLEADO", columnDefinition = "RAW(16)")
    private EmpleadosEntity Empleado;

    @ManyToOne
    @JoinColumn(name = "IDTIPOMANTENIMIENTO", referencedColumnName = "IDTIPOMANTENIMIENTO", columnDefinition = "RAW(16)")
    private TiposMantenimientoEntity TipoMantenimiento;

    @Column(name = "FECHAMANTENIMIENTO")
    private LocalDate fechaMantenimiento;

    @Column(name = "HORAINICIOMANTENIMIENTO")
    private LocalTime horaInicioMantenimiento;

    @Column(name = "HORAFINMANTENIMIENTO")
    private LocalTime horaFinMantenimiento;

    @Column(name = "OBSERVACIONMANTENIMIENTO", length = 300)
    private String observacionMantenimiento;
}
