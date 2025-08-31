package grupoExpo.API.Entities.DetallesServicioEvento;

import grupoExpo.API.Entities.Eventos.EventosEntity;
import grupoExpo.API.Entities.Servicios.ServiciosEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "DETALLESSERVICIOEVENTO")
@Getter @Setter @ToString @EqualsAndHashCode
public class DetallesServicioEventoEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDDETALLESERVICIOEVENTO", columnDefinition = "RAW(16)")
    private String idDetalleServicioEvento;

    @ManyToOne
    @JoinColumn(name = "IDSERVICIO", referencedColumnName = "IDSERVICIO", columnDefinition = "RAW(16)")
    private ServiciosEntity Servicio;

    @ManyToOne
    @JoinColumn(name = "IDEVENTO", referencedColumnName = "IDEVENTO", columnDefinition = "RAW(16)")
    private EventosEntity Evento;
}
