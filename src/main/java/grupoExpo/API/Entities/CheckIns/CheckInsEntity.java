package grupoExpo.API.Entities.CheckIns;

import grupoExpo.API.Entities.DetallesReserva.DetallesReservaEntity;
import grupoExpo.API.Entities.Empleados.EmpleadosEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHECKINS")
@Getter @Setter @ToString @EqualsAndHashCode
public class CheckInsEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDCHECKIN", columnDefinition = "RAW(16)")
    private String idCheckIn;

    @ManyToOne
    @JoinColumn(name = "IDDETALLE", referencedColumnName = "IDDETALLE", columnDefinition = "RAW(16)")
    private DetallesReservaEntity Detalle;

    @ManyToOne
    @JoinColumn(name = "IDEMPLEADO", referencedColumnName = "IDEMPLEADO", columnDefinition = "RAW(16)")
    private EmpleadosEntity Empleado;

    @Column(name = "FECHAYHORACHECKIN")
    private LocalDateTime fechaYHoraCheckIn;

    @Column(name = "OBSERVACIONCHECKIN", length = 300)
    private String observacionCheckIn;
}