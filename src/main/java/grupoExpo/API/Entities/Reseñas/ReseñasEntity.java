package grupoExpo.API.Entities.Reseñas;

import grupoExpo.API.Entities.Clientes.ClientesEntity;
import grupoExpo.API.Entities.Hotel.HotelEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "RESEÑAS")
@Getter @Setter @ToString @EqualsAndHashCode
public class ReseñasEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDRESEÑA", columnDefinition = "RAW(16)")
    private String idReseña;

    @ManyToOne
    @JoinColumn(name = "IDCLIENTE", referencedColumnName = "IDCLIENTE", columnDefinition = "RAW(16)")
    private ClientesEntity Cliente;

    @ManyToOne
    @JoinColumn(name = "IDHOTEL", referencedColumnName = "IDHOTEL", columnDefinition = "RAW(16)")
    private HotelEntity Hotel;

    @Column(name = "COMENTARIORESEÑA", length = 500)
    private String comentarioReseña;

    @Column(name = "CALIFICACIONRESEÑA")
    private int calificacionReseña;
}
