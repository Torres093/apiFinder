package grupoExpo.API.Entities.Eventos;

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
import java.time.LocalDate;

@Entity
@Table(name = "EVENTOS")
@Getter @Setter @ToString @EqualsAndHashCode
public class EventosEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDEVENTO", columnDefinition = "RAW(16)")
    private String idEvento;

    @ManyToOne
    @JoinColumn(name = "IDHOTEL", referencedColumnName = "IDHOTEL", columnDefinition = "RAW(16)")
    private HotelEntity Hotel;

    @Column(name = "NOMBREEVENTO", length = 80)
    private String nombreEvento;

    @Column(name = "DESCRIPCIONEVENTO", length = 300)
    private String descripcionEvento;

    @Column(name = "FECHAEVENTO")
    private LocalDate fechaEvento;

    @Column(name = "CAPACIDADEVENTO")
    private int capacidadEvento;

    @Column(name = "PRECIOEVENTO", precision = 10, scale = 2)
    private BigDecimal precioEvento;
}
