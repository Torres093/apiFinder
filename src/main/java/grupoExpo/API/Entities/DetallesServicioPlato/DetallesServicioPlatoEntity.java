package grupoExpo.API.Entities.DetallesServicioPlato;

import grupoExpo.API.Entities.Platos.PlatosEntity;
import grupoExpo.API.Entities.Servicios.ServiciosEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DETALLESSERVICIOPLATO")
@Getter @Setter @ToString @EqualsAndHashCode
public class DetallesServicioPlatoEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDDETALLESERVICIOPLATO", columnDefinition = "RAW(16)")
    private String idDetalleServicioPlato;

    @ManyToOne
    @JoinColumn(name = "IDSERVICIO", referencedColumnName = "IDSERVICIO", columnDefinition = "RAW(16)")
    private ServiciosEntity Servicio;

    @ManyToOne
    @JoinColumn(name = "IDPLATO", referencedColumnName = "IDPLATO", columnDefinition = "RAW(16)")
    private PlatosEntity Plato;
}
