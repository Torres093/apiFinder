package grupoExpo.API.Entities.Cargos;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CARGOS")
@Getter @Setter @ToString @EqualsAndHashCode
public class CargosEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDCARGO", columnDefinition = "RAW(16)")
    private String idCargo;

    @Column(name = "NOMBRECARGO", length = 40)
    private String nombreCargo;
}
