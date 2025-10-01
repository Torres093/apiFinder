package grupoExpo.API.Entities.Roles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import grupoExpo.API.Entities.Usuarios.UsuariosEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "ROLES")
@Getter @Setter @ToString @EqualsAndHashCode
public class RolesEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDROL", columnDefinition = "RAW(16)")
    private String idRol;

    @Column(name = "NOMBREROL", length = 30)
    private String nombreRol;

    @OneToMany(mappedBy = "Rol", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JsonIgnore
    private List<UsuariosEntity> usuarios = new ArrayList<>();

    @Override
    public String toString() {
        return "RolesEntity{" +
                "idRol='" + idRol + '\'' +
                ", nombreRol='" + nombreRol + '\'' +
                ", usuarios=" + usuarios +
                '}';
    }
}
