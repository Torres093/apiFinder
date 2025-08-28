package grupoExpo.API.Entities.DetallesReservaServicio;

import grupoExpo.API.Entities.Reservas.ReservasEntity;
import grupoExpo.API.Entities.Servicios.ServiciosEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DETALLESRESERVASERVICIO")
@Getter @Setter @ToString @EqualsAndHashCode
public class DetallesReservaServicioEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDDETALLERESERVASERVICIO", columnDefinition = "RAW(16)")
    private String idDetalleReservaServicio;

    @ManyToOne
    @JoinColumn(name = "IDRESERVA", referencedColumnName = "IDRESERVA", columnDefinition = "RAW(16)")
    private ReservasEntity Reserva;

    @ManyToOne
    @JoinColumn(name = "IDSERVICIO", referencedColumnName = "IDSERVICIO", columnDefinition = "RAW(16)")
    private ServiciosEntity Servicio;
}
