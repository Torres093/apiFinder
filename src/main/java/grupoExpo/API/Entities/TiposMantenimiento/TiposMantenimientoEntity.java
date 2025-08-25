package grupoExpo.API.Entities.TiposMantenimiento;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TIPOSMANTENIMIENTO")
@Getter @Setter @ToString @EqualsAndHashCode
public class TiposMantenimientoEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDTIPOMANTENIMIENTO", columnDefinition = "RAW(16)")
    private String idTipoMantenimiento;

    @Column(name = "NOMBRETIPOMANTENIMIENTO", length = 50)
    private String nombreTipoMantenimiento;
}
