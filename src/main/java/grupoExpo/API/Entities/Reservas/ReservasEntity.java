package grupoExpo.API.Entities.Reservas;

import grupoExpo.API.Entities.Clientes.ClientesEntity;
import grupoExpo.API.Entities.EstadosReserva.EstadosReservaEntity;
import grupoExpo.API.Entities.MetodosPago.MetodosPagoEntity;
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

    @ManyToOne
    @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE", columnDefinition = "RAW(16)")
    private ClientesEntity Cliente;

    @ManyToOne
    @JoinColumn(name = "IDESTADORESERVA", referencedColumnName = "IDESTADORESERVA", columnDefinition = "RAW(16)")
    private EstadosReservaEntity EstadoReserva;

    @ManyToOne
    @JoinColumn(name = "IDMETODOPAGO", referencedColumnName = "IDMETODOPAGO", columnDefinition = "RAW(16)")
    private MetodosPagoEntity MetodoPago;

    @Column(name = "FECHARESERVA")
    private LocalDate fechaReserva;

    @Column(name = "PRECIOTOTALRESERVA", precision = 14, scale = 2)
    private BigDecimal precioTotalReserva;
}
