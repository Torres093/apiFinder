package grupoExpo.API.Entities.Habitaciones;

import grupoExpo.API.Utils.Convertidor;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.UUID;

@Entity
@Table(name = "HABITACIONES")
@Getter @Setter @ToString @EqualsAndHashCode
public class HabitacionesEntity {


    //@Convert(converter = Convertidor.class)
    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDHABITACION", columnDefinition = "RAW(16)")
    private String idHabitacion;

    //@Convert(converter = Convertidor.class)
    @Column(name = "IDTIPOHABITACION", columnDefinition = "RAW(16)")
    private String idTipoHabitacion;

    //@Convert(converter = Convertidor.class)
    @Column(name = "IDHOTEL", columnDefinition = "RAW(16)")
    private String idHotel;

    //@Convert(converter = Convertidor.class)
    @Column(name = "IDESTADOHABITACION", columnDefinition = "RAW(16)")
    private String idEstadoHabitacion;

    @Column(name = "NUMEROHABITACION")
    private int numeroHabitacion;

    @Column(name = "DESCRIPCIONHABITACION", length = 500)
    private String descripcionHabitacion;

    @Column(name = "PRECIOHABITACION", precision = 7, scale = 2)
    private BigDecimal precioHabitacion;

    @Column(name = "CAPACIDADHABITACION")
    private int capacidadHabitacion;


}
