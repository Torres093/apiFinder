package grupoExpo.API.Exceptions.Clientes;

public class ExcepcionClienteNoRegistrado extends RuntimeException {
    public ExcepcionClienteNoRegistrado(String message) {
        super(message);
    }
}
