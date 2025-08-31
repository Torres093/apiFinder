package grupoExpo.API.Entities.Platos;

import grupoExpo.API.Entities.Hotel.HotelEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;

@Entity
@Table(name = "PLATOS")
@Getter @Setter @ToString @EqualsAndHashCode
public class PlatosEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDPLATO", columnDefinition = "RAW(16)")
    private String idPlato;

    @ManyToOne
    @JoinColumn(name = "IDHOTEL", referencedColumnName = "IDHOTEL", columnDefinition = "RAW(16)")
    private HotelEntity Hotel;

    @Column(name = "NOMBREPLATO", length = 100)
    private String nombrePlato;

    @Column(name = "DESCRIPCIONPLATO", length = 300)
    private String descripcionPlato;

    @Column(name = "PRECIOPLATO", precision = 10, scale = 2)
    private BigDecimal precioPlato;
}
