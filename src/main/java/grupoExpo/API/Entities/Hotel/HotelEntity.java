package grupoExpo.API.Entities.Hotel;

import ch.qos.logback.core.model.NamedModel;
import grupoExpo.API.Entities.TiposHotel.TiposHotelEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Table(name = "HOTEL")
@Getter @Setter @ToString @EqualsAndHashCode
public class HotelEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDHOTEL", columnDefinition = "RAW(16)")
    private String idHotel;

    @ManyToOne
    @JoinColumn(name = "IDTIPOHOTEL", referencedColumnName = "IDTIPOHOTEL", columnDefinition = "RAW(16)")
    private TiposHotelEntity TipoHotel;

    @Column(name = "NOMBREHOTEL", length = 100)
    private String nombreHotel;

    @Column(name = "UBICACIONHOTEL", length = 100)
    private String ubicacionHotel;

    @Column(name = "CORREOHOTEL", length = 200, unique = true)
    private String correoHotel;

    @Column(name = "FECHACREACIONHOTEL")
    private LocalDate fechaCreacionHotel;

    @Column(name = "TELEFONOHOTEL", length = 15)
    private String telefonoHotel;

    @Column(name = "IMAGENHOTEL", length = 4000)
    private String imagenHotel;

    @Column(name = "NUMEROHABITACIONESHOTEL")
    private int numeroHabitacionesHotel;
}
