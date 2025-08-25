package grupoExpo.API.Entities.Habitaciones;

import grupoExpo.API.Entities.EstadosHabitacion.EstadosHabitacionEntity;
import grupoExpo.API.Entities.Hotel.HotelEntity;
import grupoExpo.API.Entities.TiposHabitacion.TiposHabitacionEntity;
import grupoExpo.API.Utils.Convertidor;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
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
    @ManyToOne
    @JoinColumn(name = "IDTIPOHABITACION", referencedColumnName = "IDTIPOHABITACION", columnDefinition = "RAW(16)")
    private TiposHabitacionEntity TipoHabitacion;

    //@Convert(converter = Convertidor.class)
    @ManyToOne
    @JoinColumn(name = "IDHOTEL", referencedColumnName = "IDHOTEL", columnDefinition = "RAW(16)")
    private HotelEntity Hotel;

    //@Convert(converter = Convertidor.class)
    @ManyToOne
    @JoinColumn(name = "IDESTADOHABITACION", referencedColumnName = "IDESTADOHABITACION", columnDefinition = "RAW(16)")
    private EstadosHabitacionEntity EstadoHabitacion;

    @Column(name = "NUMEROHABITACION")
    private int numeroHabitacion;

    @Column(name = "DESCRIPCIONHABITACION", length = 500)
    private String descripcionHabitacion;

    @Column(name = "PRECIOHABITACION", precision = 7, scale = 2)
    @Digits(integer = 5, fraction = 2, message = "El valor debe tener como maximo 5 digitos enteros y 2 decimales")
    private BigDecimal precioHabitacion;

    @Column(name = "CAPACIDADHABITACION")
    private int capacidadHabitacion;


}
