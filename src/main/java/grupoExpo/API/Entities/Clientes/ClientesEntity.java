package grupoExpo.API.Entities.Clientes;


import grupoExpo.API.Utils.Convertidor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.GenericGenerator;

import java.util.Date;
import java.util.UUID;

@Entity
@Table(name = "CLIENTES")
@Getter @Setter @ToString @EqualsAndHashCode
public class ClientesEntity {


    //@Convert(converter = Convertidor.class)
    @Id
    @GenericGenerator(name = "db-uuid", strategy = "guid")
    @GeneratedValue(generator = "db-uuid")
    @Column(name = "IDCLIENTE", columnDefinition = "RAW(16)")
    private String idCliente;

    //@Convert(converter = Convertidor.class)
    @Column(name = "IDUSUARIO", columnDefinition = "RAW(16)", unique = true)
    private String idUsuario;

    @Column(name = "NOMBRECLIENTE", length = 60)
    private String nombreCliente;

    @Column(name = "APELLIDOCLIENTE", length = 60)
    private String apellidoCliente;

    @Column(name = "DUICLIENTE", length = 10, unique = true)
    private String duiCliente;

    @Column(name = "NACIMIENTOCLIENTE")
    @Temporal(TemporalType.DATE)
    private Date nacimientoCliente;

}
