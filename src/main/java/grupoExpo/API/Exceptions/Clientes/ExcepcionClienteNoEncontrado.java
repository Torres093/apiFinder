package grupoExpo.API.Exceptions.Clientes;

public class ExcepcionClienteNoEncontrado extends RuntimeException {
    public ExcepcionClienteNoEncontrado(String message) {
        super(message);
    }
}
