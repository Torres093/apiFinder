package grupoExpo.API.Entities.CheckOuts;

import grupoExpo.API.Entities.DetallesReserva.DetallesReservaEntity;
import grupoExpo.API.Entities.Empleados.EmpleadosEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHECKOUTS")
@Getter @Setter @ToString @EqualsAndHashCode
public class CheckOutsEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDCHECKOUT", columnDefinition = "RAW(16)")
    private String idCheckOut;

    @ManyToOne
    @JoinColumn(name = "IDDETALLE", referencedColumnName = "IDDETALLE", columnDefinition = "RAW(16)")
    private DetallesReservaEntity Detalle;

    @ManyToOne
    @JoinColumn(name = "IDEMPLEADO", referencedColumnName = "IDEMPLEADO", columnDefinition = "RAW(16)")
    private EmpleadosEntity Empleado;

    @Column(name = "FECHAYHORACHECKOUT")
    private LocalDateTime fechaYHoraCheckOut;

    @Column(name = "OBSERVACIONCHECKOUT", length = 300)
    private String observacionCheckOut;
}
