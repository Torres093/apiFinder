package grupoExpo.API.Entities.Empleados;

import grupoExpo.API.Entities.Usuarios.UsuariosEntity;
import grupoExpo.API.Entities.Cargos.CargosEntity;
import grupoExpo.API.Entities.Hotel.HotelEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "EMPLEADOS")
@Getter @Setter @ToString @EqualsAndHashCode
public class EmpleadosEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDEMPLEADO", columnDefinition = "RAW(16)")
    private String idEmpleado;

    @OneToOne
    @JoinColumn(name = "IDUSUARIO", referencedColumnName = "IDUSUARIO", columnDefinition = "RAW(16)", unique = true)
    private UsuariosEntity Usuario;

    @ManyToOne
    @JoinColumn(name = "IDCARGO", referencedColumnName = "IDCARGO", columnDefinition = "RAW(16)")
    private CargosEntity Cargo;

    @ManyToOne
    @JoinColumn(name = "IDHOTEL", referencedColumnName = "IDHOTEL", columnDefinition = "RAW(16)")
    private HotelEntity Hotel;

    @Column(name = "NOMBREEMPLEADO", length = 60)
    private String nombreEmpleado;

    @Column(name = "APELLIDOEMPLEADO", length = 60)
    private String apellidoEmpleado;

    @Column(name = "DIRECCIONEMPLEADO", length = 100)
    private String direccionEmpleado;

    @Column(name = "NACIMIENTOEMPLEADO")
    private LocalDate nacimientoEmpleado;

    @Column(name = "TELEFONOEMPLEADO", length = 15)
    private String telefonoEmpleado;

    @Column(name = "SALARIOEMPLEADO", precision = 7, scale = 2)
    private BigDecimal salarioEmpleado;

    @Column(name = "DUIEMPLEADO", length = 10, unique = true)
    private String duiEmpleado;
}
