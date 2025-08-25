package grupoExpo.API.Entities.MetodosPago;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "METODOSPAGO")
@Getter @Setter @ToString @EqualsAndHashCode
public class MetodosPagoEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDMETODOPAGO", columnDefinition = "RAW(16)")
    private String idMetodoPago;

    @Column(name = "NOMBREMETODOPAGO", length = 50)
    private String nombreMetodoPago;
}
