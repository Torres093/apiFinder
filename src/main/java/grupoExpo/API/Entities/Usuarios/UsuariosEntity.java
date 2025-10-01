package grupoExpo.API.Entities.Usuarios;

import grupoExpo.API.Entities.Roles.RolesEntity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

@Entity
@Table(name = "USUARIOS")
@Getter @Setter @ToString @EqualsAndHashCode
public class UsuariosEntity {

    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDUSUARIO", columnDefinition = "RAW(16)")
    private String idUsuario;

    @ManyToOne
    @JoinColumn(name = "IDROL", referencedColumnName = "IDROL", columnDefinition = "RAW(16)")
    private RolesEntity Rol;

    @Column(name = "NOMBREUSUARIO", length = 100, unique = true)
    private String nombreUsuario;

    @Column(name = "CORREOUSUARIO", length = 200, unique = true)
    private String correoUsuario;

    @Column(name = "CONTRASEÑAUSUARIO", length = 250)
    private String contraseñaUsuario;

    @Column(name = "SEGURITYANSWERUSUARIO", length = 250)
    private String segurityAnswerUsuario;

    @Column(name = "IMAGENUSUARIO", length = 4000)
    private String imagenUsuario;

    @Column(name = "GENEROUSUARIO")
    private char generoUsuario;

    @Override
    public String toString() {
        return "UsuariosEntity{" +
                "idUsuario='" + idUsuario + '\'' +
                ", Rol=" + Rol +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", correoUsuario='" + correoUsuario + '\'' +
                ", contraseñaUsuario='" + contraseñaUsuario + '\'' +
                ", segurityAnswerUsuario='" + segurityAnswerUsuario + '\'' +
                ", imagenUsuario='" + imagenUsuario + '\'' +
                ", generoUsuario=" + generoUsuario +
                '}';
    }
}
