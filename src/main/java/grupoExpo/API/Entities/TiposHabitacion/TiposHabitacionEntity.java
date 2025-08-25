package grupoExpo.API.Entities.TiposHabitacion;

import grupoExpo.API.Entities.CategoriasTipoHabitacion.CategoriasTipoHabitacionEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "TIPOSHABITACION")
@Getter @Setter @ToString @EqualsAndHashCode
public class TiposHabitacionEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDTIPOHABITACION", columnDefinition = "RAW(16)")
    private String idTipoHabitacion;

    @ManyToOne
    @JoinColumn(name = "IDCATEGORIATIPOHABITACION", referencedColumnName = "IDCATEGORIATIPOHABITACION", columnDefinition = "RAW(16)")
    private CategoriasTipoHabitacionEntity CategoriaTipoHabitacion;

    @Column(name = "NOMBRETIPOHABITACION", length = 50)
    private String nombreTipoHabitacion;

    @Column(name = "DESCRIPCIONTIPOHABITACION", length = 200)
    private String descripcionTipoHabitacion;
}
