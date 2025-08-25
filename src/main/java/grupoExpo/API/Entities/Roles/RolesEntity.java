package grupoExpo.API.Entities.Roles;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

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
}
