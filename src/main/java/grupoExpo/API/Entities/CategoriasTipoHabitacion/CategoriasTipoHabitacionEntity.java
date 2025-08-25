package grupoExpo.API.Entities.CategoriasTipoHabitacion;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "CATEGORIASTIPOHABITACION")
@Getter @Setter @ToString @EqualsAndHashCode
public class CategoriasTipoHabitacionEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDCATEGORIATIPOHABITACION", columnDefinition = "RAW(16)")
    private String idCategoriaTipoHabitacion;

    @Column(name = "NOMBRECATEGORIATIPOHABITACION", length = 50)
    private String nombreCategoriaTipoHabitacion;
}
