package grupoExpo.API.Entities.Clientes;


import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Date;

@Entity
@Table(name = "CLIENTES")
@Getter @Setter @ToString @EqualsAndHashCode
public class ClientesEntity {

    @Id
    @Column(name = "IDCLIENTE", columnDefinition = "RAW(16)")
    private byte[] idCliente;

    @Column(name = "IDUSUARIO", columnDefinition = "RAW(16)", unique = true)
    private byte[] idUsuario;

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
