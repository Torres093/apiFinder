package grupoExpo.API.Entities.TiposHotel;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TIPOSHOTEL")
@Getter @Setter @ToString @EqualsAndHashCode
public class TiposHotelEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDTIPOHOTEL", columnDefinition = "RAW(16)")
    private String idTipoHotel;

    @Column(name = "NOMBRETIPOHOTEL", length = 50)
    private String nombreTipoHotel;
}
