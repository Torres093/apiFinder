package grupoExpo.API.Entities.EstadosReserva;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ESTADOSRESERVA")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadosReservaEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDESTADORESERVA", columnDefinition = "RAW(16)")
    private String idEstadoReserva;

    @Column(name = "NOMBREESTADORESERVA", length = 50)
    private String nombreEstadoReserva;
}