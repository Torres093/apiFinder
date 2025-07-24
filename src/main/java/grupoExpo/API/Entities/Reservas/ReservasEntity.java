package grupoExpo.API.Entities.Reservas;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "RESERVAS")
@Getter @Setter @ToString @EqualsAndHashCode
public class ReservasEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDRESERVA", columnDefinition = "RAW(16)")
    private String idReserva;

    @Column(name = "IDCLIENTE", columnDefinition = "RAW(16)")
    private String idCliente;

    @Column(name = "IDESTADORESERVA", columnDefinition = "RAW(16)")
    private String idEstadoReserva;

    @Column(name = "IDMETODOPAGO", columnDefinition = "RAW(16)")
    private String idMetodoPago;

    @Column(name = "FECHARESERVA")
    @Temporal(TemporalType.DATE)
    private Date fechaReserva;

    @Column(name = "PRECIOTOTALRESERVA", precision = 14, scale = 2)
    private BigDecimal precioTotalReserva;
}
