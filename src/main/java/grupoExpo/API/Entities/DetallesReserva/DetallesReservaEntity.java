package grupoExpo.API.Entities.DetallesReserva;

import grupoExpo.API.Entities.Habitaciones.HabitacionesEntity;
import grupoExpo.API.Entities.Reservas.ReservasEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "DETALLESRESERVA")
@Getter @Setter @ToString @EqualsAndHashCode
public class DetallesReservaEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDDETALLE", columnDefinition = "RAW(16)")
    private String idDetalle;

    @ManyToOne
    @JoinColumn(name = "IDRESERVA", referencedColumnName = "IDRESERVA", columnDefinition = "RAW(16)")
    private ReservasEntity Reserva;

    @ManyToOne
    @JoinColumn(name = "IDHABITACION", referencedColumnName = "IDHABITACION", columnDefinition = "RAW(16)")
    private HabitacionesEntity Habitacion;

    @Column(name = "NUMEROHUESPEDESDETALLE")
    private int numeroHuespedesDetalle;

    @Column(name = "FECHAYHORADELLEGADADETALLE")
    private LocalDateTime fechaYHoraDeLlegadaDetalle;

    @Column(name = "FECHAYHORADESALIDADETALLE")
    private LocalDateTime fechaYHoraDeSalidaDetalle;

    @Column(name = "PRECIODETALLE", precision = 7, scale = 2)
    private BigDecimal precioDetalle;

    @Column(name = "DESCUENTODETALLE")
    private int descuentoDetalle;
}
