package grupoExpo.API.Exceptions.Clientes;

import lombok.Getter;

public class ExcepcionDatosDuplicadosCliente extends RuntimeException {

    @Getter
    private String campoDuplicado;

    public ExcepcionDatosDuplicadosCliente(String message, String campoDuplicado) {
        super(message);
        this.campoDuplicado = campoDuplicado;
    }

    public ExcepcionDatosDuplicadosCliente(String campoDuplicado){
        this.campoDuplicado = campoDuplicado;
    }
}
