package grupoExpo.API.Exceptions.MetodosPago;

public class ExcepcionMetodoPagoNoEncontrado extends RuntimeException {
    public ExcepcionMetodoPagoNoEncontrado(String message) {
        super(message);
    }
}
