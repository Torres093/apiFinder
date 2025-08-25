package grupoExpo.API.Entities.Servicios;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "SERVICIOS")
@Getter @Setter @ToString @EqualsAndHashCode
public class ServiciosEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDSERVICIO", columnDefinition = "RAW(16)")
    private String idServicio;

    @Column(name = "NOMBRESERVICIO", length = 100)
    private String nombreServicio;

    @Column(name = "DESCRIPCIONSERVICIO", length = 500)
    private String descripcionServicio;
}
