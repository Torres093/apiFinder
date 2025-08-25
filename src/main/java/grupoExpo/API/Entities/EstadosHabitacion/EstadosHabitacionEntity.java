package grupoExpo.API.Entities.EstadosHabitacion;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "ESTADOSHABITACION")
@Getter @Setter @ToString @EqualsAndHashCode
public class EstadosHabitacionEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDESTADOHABITACION", columnDefinition = "RAW(16)")
    private String idEstadoHabitacion;

    @Column(name = "NOMBREESTADOHABITACION", length = 30)
    private String nombreEstadoHabitacion;
}